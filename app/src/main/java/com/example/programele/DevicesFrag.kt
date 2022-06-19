package com.example.programele

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.card.*
import kotlinx.android.synthetic.main.fragment_devices.*

val user1 = Firebase.auth.currentUser

class DevicesFrag : Fragment() {

    val auth = FirebaseAuth.getInstance()
    val authUser = auth.currentUser
    val db = FirebaseFirestore.getInstance()
    val docRef = db.collection("users").document(user?.uid.toString())

    var dialog: AlertDialog? = null
    var layout: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_devices, container, false)
    }

    override fun onResume() {
        super.onResume()

        Log.d(ContentValues.TAG, "uid " + user?.uid.toString())

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }

        devices_addDevice.setOnClickListener {
            openFragment()
        }

        db.collection("devices")
            .whereEqualTo("owner",  authUser?.uid.toString())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val dbName = document.get("name").toString()
                    val dbAmount = document.get("amount").toString() + " W"
                    val dbClass = document.get("class").toString()
                    val dbType = document.get("type").toString()

                    val view: View = layoutInflater.inflate(R.layout.card, null)
                    val nameView = view.findViewById<TextView>(R.id.name)
                    val amountView = view.findViewById<TextView>(R.id.efficiency)
                    val classView = view.findViewById<TextView>(R.id.energyClass)
                    val typeView = view.findViewById<TextView>(R.id.deviceType)
                    val delete = view.findViewById<ImageView>(R.id.delete)
                    val edit = view.findViewById<ImageView>(R.id.card_edit)
                    nameView.text = dbName
                    amountView.text = dbAmount
                    classView.text = dbClass
                    typeView.text = dbType



                    delete.setOnClickListener {

                        val dialogClickListener =
                            DialogInterface.OnClickListener { dialog, which ->
                                when (which) {
                                    DialogInterface.BUTTON_POSITIVE -> {
                                        container.removeView(view)
                                        if (container.isEmpty()){
                                            val view2: View = layoutInflater.inflate(R.layout.emptytext, null)
                                            container.addView(view2)
                                        }
                                        db.collection("devices").document(document.id)
                                            .delete()
                                            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                                            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
                                    }
                                    DialogInterface.BUTTON_NEGATIVE -> {}
                                }
                            }

                        val builder = AlertDialog.Builder(context)
                        builder.setMessage("Ar tikrai norite panaikinti prietaisą?")
                            .setPositiveButton("Taip", dialogClickListener)
                            .setNegativeButton("Ne", dialogClickListener).show()
                    }
                    edit.setOnClickListener {
                        val activity: Activity? = activity
                        val intent = Intent(
                            activity,
                            EditDeviceFrag::class.java
                        )
                        intent.putExtra("deviceID", document.id)
                        openFragment2()
                    }


                    container.addView(view)
                }

                if (container.isEmpty()){
                    val view2: View = layoutInflater.inflate(R.layout.emptytext, null)
                    container.addView(view2)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    private fun openFragment() {
        val fragmentManager = (activity as FragmentActivity).supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_wrapper, AddDeviceFrag()).addToBackStack(null).commit();
    }

    private fun addCard(name: String, amount: String, effClass: String, devType: String) {
        val view: View = layoutInflater.inflate(R.layout.card, null)
        val nameView = view.findViewById<TextView>(R.id.name)
        val amountView = view.findViewById<TextView>(R.id.efficiency)
        val classView = view.findViewById<TextView>(R.id.energyClass)
        val typeView = view.findViewById<TextView>(R.id.deviceType)
        val delete = view.findViewById<ImageView>(R.id.delete)
        nameView.text = name
        amountView.text = amount
        classView.text = effClass
        typeView.text = devType

        delete.setOnClickListener {
            container.removeView(view)
        }

        container.addView(view)
    }


    private fun openFragment2() {
        val fragmentManager = (activity as FragmentActivity).supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_wrapper, EditDeviceFrag()).addToBackStack("tag").commit();
    }

}