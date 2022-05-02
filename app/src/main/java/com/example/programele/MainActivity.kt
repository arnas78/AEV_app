package com.example.programele

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.system.Os.close
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var toggle : ActionBarDrawerToggle

    lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerlayout)
        val navView: NavigationView = findViewById<NavigationView>(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener{

            it.isChecked = true

            when(it.itemId){
                R.id.nav_home -> replaceFragment(HomeFrag(), it.title.toString())
                R.id.nav_settings -> replaceFragment(SettingsFrag(), it.title.toString())
                R.id.nav_review -> replaceFragment(FeedbackFrag(), it.title.toString())
                R.id.nav_device -> replaceFragment(DevicesFrag(), it.title.toString())
                R.id.nav_levels -> replaceFragment(LevelFrag(), it.title.toString())

            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment, title : String){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
        drawerLayout.closeDrawers()
        setTitle(title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {

            return true
        }
        return super.onOptionsItemSelected(item)
    }
}