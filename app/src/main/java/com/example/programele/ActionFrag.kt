package com.example.programele

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_action.*
import kotlinx.android.synthetic.main.fragment_add_device.*
import kotlinx.android.synthetic.main.fragment_devices.*

private lateinit var auth: FirebaseAuth

class ActionFrag : Fragment() {

    private var devicePosition: Int = 0
    private lateinit var deviceSelection: String
    private lateinit var devicePowerSelection: String
    private lateinit var timeSelection: String
    private lateinit var deviceIdSelection: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_action, container, false)
    }

    override fun onResume() {
        super.onResume()

        auth = FirebaseAuth.getInstance()
        val authUser = auth.currentUser
        val db = FirebaseFirestore.getInstance()

        val devicesArr: MutableList<String> = ArrayList()
        val devicePowerArr: MutableList<String> = ArrayList()


        db.collection("devices")
            .whereEqualTo("owner",  authUser?.uid.toString())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                    val dbName = document.get("name").toString()
                    devicesArr.add(dbName)
                    devicePowerArr.add(document.get("amount").toString())
                }

            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }

        val arrayAdapterDevices = ArrayAdapter(requireContext(), R.layout.dropdown_item, devicesArr)
        action_deviceAutoCompleteTextView.setAdapter(arrayAdapterDevices)

        val time = resources.getStringArray(R.array.time)
        val arrayAdapterTime = ArrayAdapter(requireContext(), R.layout.dropdown_item, time)
        action_timeAutoCompleteTextView.setAdapter(arrayAdapterTime)

        deviceSelection = ""
        timeSelection = ""
        devicePowerSelection = ""
        deviceIdSelection = ""
        devicePosition = 0

        action_deviceAutoCompleteTextView.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, rowId ->
            deviceSelection = parent.getItemAtPosition(position) as String
            devicePowerSelection = devicePowerArr[position]
        })

        action_timeAutoCompleteTextView.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, rowId ->
            timeSelection = parent.getItemAtPosition(position) as String
            devicePosition = position
        })

        action_btnSave.setOnClickListener {
            if (deviceSelection != "" && timeSelection != "") {
                val docName = "" + System.currentTimeMillis()
                val activity: Activity? = activity
                val device = hashMapOf(
                    "time" to devicePosition,
                    "power" to devicePowerSelection,
                    "owner" to authUser?.uid.toString()
                )

                Log.d(TAG,"laikas $timeSelection")
                // Add a new document with an auth generated ID
                db.collection("actions").document(docName).set(device)
                Toast.makeText(activity, "Sėkmingai pridėjote veiklą!", Toast.LENGTH_SHORT).show()
            }
            else if (deviceSelection.equals("")){
                Toast.makeText(activity, "Pasirinkite prietaisą", Toast.LENGTH_SHORT).show()
            }
            else if (timeSelection.equals("")){
                Toast.makeText(activity, "Pasirinkite laiką, kiek naudojote prietaisą", Toast.LENGTH_SHORT).show()
            }
        }


    }

}