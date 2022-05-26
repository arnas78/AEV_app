package com.example.programele

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.facebook.FacebookSdk.getApplicationContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_level.*
import kotlinx.android.synthetic.main.fragment_level.userProfile_profilePicture
import java.util.*


class LevelFrag : Fragment() {

    private var storageReference: StorageReference? = null
    private lateinit var imageUri: Uri
    private var firestoreEmail: String? = null
    private var firestoreName: String? = null
    private var firestoreSurname: String? = null
    private lateinit var auth: FirebaseAuth
    private val storage: FirebaseStorage? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_level, container, false)
    }


    override fun onResume() {
        super.onResume()

        getFirestoreVariables()

        val storage: FirebaseStorage?
        val storageReference: StorageReference?
        val fAuth: FirebaseAuth?
        auth = FirebaseAuth.getInstance()

        storage = FirebaseStorage.getInstance()
        storageReference = storage.getReference()
        fAuth = FirebaseAuth.getInstance()

        val profileRef: StorageReference =
            storageReference.child("users/" + fAuth.currentUser?.uid + "/profile.jpg")

//        Picasso.get().load(auth.currentUser?.photoUrl).into(userProfile_profilePicture)

        profileRef.downloadUrl.addOnSuccessListener { uri ->
            Picasso.get().load(uri).into(userProfile_profilePicture)
        }

        userProfile_changePicture.setOnClickListener{
            choosePicture()
        }

        userProfile_profilePicture.setOnClickListener{
            choosePicture()
        }

        userProfile_btnSave.setOnClickListener{
            getFirestoreVariables()
            update_profile()
        }

    }


    fun update_profile() {

        auth = FirebaseAuth.getInstance()
        val authUser = auth.currentUser
        val activity2: Activity? = activity
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users").document(authUser?.uid.toString())


        if (firestoreSurname != userProfile_editSurname.text.toString() || firestoreName != userProfile_editName.text.toString()
            || firestoreEmail != userProfile_editEmail.text.toString()) {

            if (firestoreSurname != userProfile_editSurname.text.toString()) {
                docRef.update("surname", userProfile_editSurname.text.toString())
            }
            if (firestoreName != userProfile_editName.text.toString()) {
                docRef.update("name", userProfile_editName.text.toString())
            }
            if (firestoreEmail != userProfile_editEmail.text.toString()) {

                if (isEmailValid(userProfile_editEmail.text.toString())) {
                    docRef.update("email", userProfile_editEmail.text.toString())
                } else {
                    Toast.makeText(
                        activity2,
                        "Failed to update email, enter a valid email",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            Toast.makeText(activity2, "Duomenys išsaugoti", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(activity, "Išsaugota, jokių pakeitimų neatlikta.", Toast.LENGTH_SHORT).show()
        }
    }

    fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun getFirestoreVariables() {
        auth = FirebaseAuth.getInstance()
        val authUser = auth.currentUser
        val activity2: Activity? = activity
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users").document(authUser?.uid.toString())

        Log.d(ContentValues.TAG, "uid " + authUser?.uid.toString())

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    firestoreEmail = document.getString("email")
                    firestoreName = document.getString("name")
                    firestoreSurname = document.getString("surname")
                    userProfile_editEmail.setText(firestoreEmail)
                    userProfile_editName.setText(firestoreName)
                    userProfile_editSurname.setText(firestoreSurname)
                    Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }

    }

    //    private void loadImagePlaceholder(){
    //        Picasso.get()
    //                .load( R.drawable.loading_image )
    //                .error( R.drawable.error_image )
    //                .placeholder( R.drawable.progress_animation )
    //                .into( profileImage );
    //    }
    private fun uploadPicture() {

        val storage: FirebaseStorage?
        val storageReference: StorageReference?
        val fAuth: FirebaseAuth?
        auth = FirebaseAuth.getInstance()

        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        val activity2: Activity? = activity
        val pd = ProgressDialog(activity)
        val authUser = auth.currentUser

        pd.setTitle("Uploading Image...")
        pd.show()
        val randomKey = UUID.randomUUID().toString()
        //        StorageReference fileRef = storageReference.child("images/" + randomKey);
        //        StorageReference fileRef = storageReference.child("images/" + randomKey);
        val fileRef =
            storageReference.child("users/" + authUser?.uid + "/profile.jpg")

        fileRef.putFile(imageUri)
            .addOnSuccessListener {
                pd.dismiss()
                //                        Snackbar.make(findViewById(android.R.id.content),"Image uploaded.", Snackbar.LENGTH_LONG).show();
                fileRef.downloadUrl.addOnSuccessListener { uri ->
                    Picasso.get().load(uri).into(userProfile_profilePicture)
                }
            }
            .addOnFailureListener {
                pd.dismiss()
                Toast.makeText(getApplicationContext(), "Failed To Upload", Toast.LENGTH_LONG)
                    .show()
            }
            .addOnProgressListener { taskSnapshot ->
                val progressPercent =
                    100.00 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                pd.setMessage("Percentage: " + progressPercent.toInt() + "%")
            }
    }

    private fun choosePicture() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data!!
            //profileImage.setImageURI(imageUri);
            uploadPicture()
        }
    }

}