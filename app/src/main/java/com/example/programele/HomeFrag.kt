package com.example.programele

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentTransaction


class HomeFrag : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val button = view.findViewById<Button>(R.id.tips)
        button.setOnClickListener{
         val patarimaiFrag = PatarimaiFrag()
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.homefrag,patarimaiFrag)
            transaction.commit()

        }
        // Inflate the layout for this fragment
        return view
    }


}