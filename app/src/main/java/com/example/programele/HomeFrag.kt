package com.example.programele


import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import io.grpc.Context
import kotlinx.android.synthetic.main.fragment_home.*


val user = Firebase.auth.currentUser

private var myStr: String? = null

private var tvMyText: TextView? = null

class HomeFrag : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data = arguments

        if (data != null) {
            myStr = data.getString("myData")
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        // Inflate the layout for this fragment
        return view
    }

    override fun onResume() {
        super.onResume()

        auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users").document(user?.uid.toString())
        Log.d(TAG, "uid " + user?.uid.toString())

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    name = document.getString("username").toString()
                    Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }

        val storage: FirebaseStorage?
        val storageReference: StorageReference?
        val fAuth: FirebaseAuth?

        storage = FirebaseStorage.getInstance()
        storageReference = storage.getReference()
        fAuth = FirebaseAuth.getInstance()

        userHome_userGreeting.text = "Sveiki, " + auth.currentUser?.displayName
        val profileRef: StorageReference =
            storageReference.child("users/" + fAuth.currentUser?.uid + "/profile.jpg")

        Picasso.get().load(auth.currentUser?.photoUrl).into(userProfile_profilePicture)

        profileRef.downloadUrl.addOnSuccessListener { uri ->
            Picasso.get().load(uri).into(userProfile_profilePicture)
        }

    }
}