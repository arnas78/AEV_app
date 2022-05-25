package com.example.programele

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_add_device.*


private lateinit var auth: FirebaseAuth

class AddDeviceFrag : Fragment() {


    private lateinit var classSelection: String
    private lateinit var typeSelection: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_add_device, container, false)
    }

    override fun onResume() {
        super.onResume()

        auth = FirebaseAuth.getInstance()
        val languages = resources.getStringArray(R.array.deviceEfficiency)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, languages)
        autoCompleteTextView3.setAdapter(arrayAdapter)

        val types = resources.getStringArray(R.array.deviceType)
        val arrayAdapterTypes = ArrayAdapter(requireContext(), R.layout.dropdown_item, types)
        autoCompleteTextView4.setAdapter(arrayAdapterTypes)

        classSelection = ""
        typeSelection = ""

        autoCompleteTextView3.setOnItemClickListener(OnItemClickListener { parent, view, position, rowId ->
            classSelection = parent.getItemAtPosition(position) as String
        })

        autoCompleteTextView4.setOnItemClickListener(OnItemClickListener { parent, view, position, rowId ->
            typeSelection = parent.getItemAtPosition(position) as String
        })

        add_btnSave.setOnClickListener{
            saveEntries()
        }

    }

    private fun saveEntries(){
        val title = add_editTitle.text.toString()
        val amount = add_editAmount.text.toString()
        val activity: Activity? = activity
        val docName = "" + System.currentTimeMillis()

        if ( !title.equals("") && !amount.equals("") && !classSelection.equals("") && !typeSelection.equals("")) {

            val db = FirebaseFirestore.getInstance()
            val authUser = auth.currentUser
            val device = hashMapOf(
                "name" to title,
                "amount" to amount,
                "class" to classSelection,
                "type" to typeSelection,
                "owner" to authUser?.uid.toString()
            )

            // Add a new document with an auth generated ID
            db.collection("devices").document(docName).set(device)
            openFragment()
        }
        else if (title.equals("")){
            Toast.makeText(activity, "Įrašykite pavadinimą", Toast.LENGTH_SHORT).show()
        }
        else if (amount.equals("")){
            Toast.makeText(activity, "Įrašykite kiekį", Toast.LENGTH_SHORT).show()
        }
        else if (classSelection.equals("")){
            Toast.makeText(activity, "Pasirinkite vartojimo klasę", Toast.LENGTH_SHORT).show()
        }
        else if (typeSelection.equals("")){
            Toast.makeText(activity, "Pasirinkite prietaiso tipą", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openFragment() {
        val fragmentManager = (activity as FragmentActivity).supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_wrapper, DevicesFrag() ).commit();
    }



}


