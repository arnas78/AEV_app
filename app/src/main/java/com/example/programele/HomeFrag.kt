package com.example.programele


import android.app.AlertDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.nfc.Tag
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.google.android.exoplayer2.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_devices.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import kotlin.math.roundToInt


val user = Firebase.auth.currentUser





class HomeFrag : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var name: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        // Inflate the layout for this fragment
        return view
    }

    override fun onResume() {
        super.onResume()

        val content = SpannableString("Kaip sutaupyti?")
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        home_textAdvice.setText(content)

        home_textAdvice.setOnClickListener{
            openFragment()
        }

        auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users").document(user?.uid.toString())
        Log.d(TAG, "uid " + user?.uid.toString())

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    name = document.get("username").toString()
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

        userHome_userGreeting.text = "Sveiki, " +  auth.currentUser?.displayName
        val profileRef: StorageReference =
            storageReference.child("users/" + fAuth.currentUser?.uid + "/profile.jpg")

//        Picasso.get().load(auth.currentUser?.photoUrl).into(userProfile_profilePicture)

        profileRef.downloadUrl.addOnSuccessListener { uri ->
            Picasso.get().load(uri).into(userProfile_profilePicture)
        }

        home_currentDay.text = getTodaysDate()

        getPrice()


    }

    private fun openFragment() {
        val fragmentManager = (activity as FragmentActivity).supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_wrapper, PatarimaiFrag() ).commit();
    }

    private fun makeDateString(day: Int, month: Int, year: Int): String? {
        return getMonthFormat(month) + " " + day + ", " + year
    }

    private fun getTodaysDate(): String? {
        val cal = Calendar.getInstance()
        val year = cal[Calendar.YEAR]
        var month = cal[Calendar.MONTH]
        month = month + 1
        val day = cal[Calendar.DAY_OF_MONTH]
        return makeDateString(day, month, year)
    }

    private fun getMonthFormat(month: Int): String {
        if (month == 1) return "Sau"
        if (month == 2) return "Vas"
        if (month == 3) return "Kov"
        if (month == 4) return "Bal"
        if (month == 5) return "Geg"
        if (month == 6) return "Bir"
        if (month == 7) return "Lie"
        if (month == 8) return "Rūg"
        if (month == 9) return "Rug"
        if (month == 10) return "Spa"
        if (month == 11) return "Lap"
        return if (month == 12) "Gru" else "Jan"

        //default should never happen
    }

    private fun getPrice(){
        auth = FirebaseAuth.getInstance()
        val authUser = auth.currentUser
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users").document(user?.uid.toString())
        var priceOutput = 0.0
        var timeOutput = 0.0
        var kwhOutput = 0.0

        db.collection("actions")
            .whereEqualTo("owner",  authUser?.uid.toString())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    android.util.Log.d(TAG, "${document.id} => ${document.data}")

                    val dbPower : Double = document.get("power").toString().toDouble()
                    val dbTime : Double = document.get("time").toString().toDouble() + 1
                    val tarifas = 0.15
                    val valandos = (dbTime * 30) / 60
                    val kWh = (dbPower * valandos) / 1000
                    val price = kWh * tarifas
                    priceOutput += price
                    timeOutput += valandos
                    kwhOutput += kWh

                }
                val priceRoundoff = (priceOutput * 1000.0).roundToInt() / 1000.0
                val priceString = priceRoundoff.toString() + " €"
                val timeRoundoff = (timeOutput * 100.0).roundToInt() / 100.0
                val timeString = timeRoundoff.toString() + " h"
                kwhOutput = (kwhOutput / 12.0) * 100
                val limitRoundoff = (kwhOutput * 100.0).roundToInt() / 100.0
                val limitString = limitRoundoff.toString() + " %"

                home_price.text = priceString
                home_time.text = timeString
                home_limit.text = limitString


            }
            .addOnFailureListener { exception ->
                android.util.Log.w(TAG, "Error getting documents: ", exception)
            }




    }

}