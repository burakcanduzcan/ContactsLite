package com.burakcanduzcan.contactslite.ui.contacts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.burakcanduzcan.contactslite.databinding.FragmentContactsBinding
import com.burakcanduzcan.contactslite.model.Contact
import com.burakcanduzcan.contactslite.model.listContacts

class ContactsFragment : Fragment() {
    private lateinit var binding: FragmentContactsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactsBinding.inflate(layoutInflater)

        //
        listContacts.add(Contact("Abuzer", "Kadayıf", "11155566"))
        listContacts.add(Contact("Nabuzer", "Madayıf", "11155567"))
        listContacts.add(Contact("Babbuzer", "Cadayıf", "11155568"))
        //

        binding.rvContacts.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvContacts.adapter = ContactsAdapter(requireContext(), listContacts, ::itemClick, ::itemLongClick)

        return binding.root
    }

    private fun itemClick(i: Int) {
        Toast.makeText(requireContext(), "Item click function not implemented", Toast.LENGTH_SHORT).show()
    }

    private fun itemLongClick(i: Int) {
        Toast.makeText(requireContext(), "Item long click function not implemented", Toast.LENGTH_SHORT).show()
    }

}