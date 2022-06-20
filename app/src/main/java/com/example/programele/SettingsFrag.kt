package com.example.programele

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFrag : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onResume() {
        super.onResume()

        auth = FirebaseAuth.getInstance()
        settings_btnLogout.setOnClickListener{

            val dialogClickListener =
                DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            Paper.book().destroy()
                            auth.signOut()
                            val intent = Intent (getActivity(), Login::class.java)
                            activity?.startActivity(intent)
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {}
                    }
                }

            val builder = AlertDialog.Builder(context)
            builder.setMessage("Ar tikrai norite atsijungti?")
                .setPositiveButton("Taip", dialogClickListener)
                .setNegativeButton("Ne", dialogClickListener).show()
        }

        settings_review.setOnClickListener{
            openFragment()
        }

        settings_help.setOnClickListener{
            navToEmailCompose("enertus.info@gmail.com", "Support", "")
        }

        settings_rate.setOnClickListener{
            openFragment2()
        }

        settings_eula.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://enertus.netlify.app"))
            startActivity(browserIntent)
        }

        settings_facebook.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/enertus.lt"))
            startActivity(browserIntent)
        }

        settings_instagram.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/enertus_lt/"))
            startActivity(browserIntent)
        }


    }



    private fun openFragment() {
        val fragmentManager = (activity as FragmentActivity).supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_wrapper, FeedbackFrag()).addToBackStack("tag").commit();
    }




    fun navToEmailCompose(email: String, subject: String, body: String) {
        val intent = Intent(Intent.ACTION_SENDTO,
            Uri.parse("mailto:${email}"))
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(android.content.Intent.EXTRA_TEXT, body)
        startActivity(Intent.createChooser(intent, "Email"))
    }

    private fun openFragment2() {
        val fragmentManager = (activity as FragmentActivity).supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_wrapper, RateFrag()).addToBackStack("tag").commit();
    }


}