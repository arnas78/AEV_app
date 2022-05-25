package com.example.programele

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.exoplayer2.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_devices.*

private var name: String? = null

class MainActivity : AppCompatActivity() {

    private var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        openFragment()
        bottomNavigationView.menu.getItem(2).isEnabled = false

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.miHome -> makeCurrentFragment(HomeFrag())
                R.id.miDevices -> makeCurrentFragment(DevicesFrag())
                R.id.miProfile -> makeCurrentFragment(PatarimaiFrag())
                R.id.miSettings -> makeCurrentFragment(SettingsFrag())
            }
            true
        }

        fab.setOnClickListener {
            makeCurrentFragment(AddDeviceFrag())
        }



    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }

    private fun openFragment() {

        val homeFrag = HomeFrag()
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
//        val data = Bundle()
//        data.putString("myData", "Hello, $name")
//        homeFrag.arguments = data
        fragmentTransaction.replace(R.id.fl_wrapper, homeFrag).commit();
    }


}