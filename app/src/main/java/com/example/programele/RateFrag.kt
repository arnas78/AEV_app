package com.example.programele

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_rate.*


/**
 * A simple [Fragment] subclass.
 * Use the [RateFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class RateFrag : Fragment() {

    val auth = FirebaseAuth.getInstance()
    val authUser = auth.currentUser
    val db = FirebaseFirestore.getInstance()
    val docRef = db.collection("users").document(user?.uid.toString())
    val activity1: Activity? = activity
    var firestoreRate = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onResume() {
        super.onResume()

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data}")
                    firestoreRate = document.get("rate") as Double
                    Log.d(TAG, "firerate $firestoreRate")
                    rate_editRate.setText(firestoreRate.toString())
                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }

        rate_btnSave.setOnClickListener{
            if (firestoreRate.toString() != rate_editRate.text.toString()){
                Log.d(TAG, "tarifas" + rate_editRate.text)
                val rate = rate_editRate.text.toString().toDouble()
                Log.d(TAG, "tarifass" + rate)
                docRef.update("rate", rate)
                Toast.makeText(activity, "Duomenys išsaugoti", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(activity, "Išsaugota, jokių pakeitimų neatlikta.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rate, container, false)
    }




}