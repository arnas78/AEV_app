package com.example.programele

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_feedback.*


class FeedbackFrag : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feedback, container, false)
    }



    override fun onResume() {
        super.onResume()

        auth = FirebaseAuth.getInstance()
        val docName = "" + System.currentTimeMillis()
        val db = FirebaseFirestore.getInstance()
        val authUser = auth.currentUser

        feedback_btnSend.setOnClickListener{
            if (!feedback_review.text.toString().equals("")){
                val review = hashMapOf(
                    "sender" to authUser?.uid.toString(),
                    "message" to feedback_review.text.toString()
                )
                db.collection("reviews").document(docName).set(review)
                openFragment()
                Toast.makeText(activity, "Dėkojame už atsiliepimą!", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(activity, "Įveskite jūsų atsiliepimą", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun openFragment() {
        val fragmentManager = (activity as FragmentActivity).supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_wrapper, SettingsFrag() ).commit();
    }
}