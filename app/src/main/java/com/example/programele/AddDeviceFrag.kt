package com.example.programele

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_add_device.*


class AddDeviceFrag : Fragment() {

    private lateinit var classSelection: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_add_device, container, false)
    }

    override fun onResume() {
        super.onResume()

        val languages = resources.getStringArray(R.array.deviceEfficiency)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, languages)
        autoCompleteTextView3.setAdapter(arrayAdapter)

        val title = add_editTitle.text.toString()
        val amount = add_editAmount.text.toString()


        autoCompleteTextView3.setOnItemClickListener(OnItemClickListener { parent, view, position, rowId ->
            classSelection = parent.getItemAtPosition(position) as String
        })

    }


}