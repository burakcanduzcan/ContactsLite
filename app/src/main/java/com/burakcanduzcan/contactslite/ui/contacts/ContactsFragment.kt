package com.burakcanduzcan.contactslite.ui.contacts

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.burakcanduzcan.contactslite.databinding.FragmentContactsBinding
import com.burakcanduzcan.contactslite.model.Contact
import com.burakcanduzcan.contactslite.model.PhoneNumber
import com.burakcanduzcan.contactslite.model.listContacts
import com.google.android.material.snackbar.Snackbar

class ContactsFragment : Fragment() {
    private lateinit var binding: FragmentContactsBinding
    var uriToBeCalled: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentContactsBinding.inflate(layoutInflater)

        //
        listContacts.add(Contact("Abuzer", "Kadayıf", PhoneNumber(phoneNumber = "11155566")))
        listContacts.add(Contact("Nabuzer", "Madayıf", PhoneNumber(phoneNumber = "11155567")))
        listContacts.add(Contact("Babbuzer", "Cadayıf", PhoneNumber(phoneNumber = "11155568")))
        //

        binding.rvContacts.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvContacts.adapter =
            ContactsAdapter(requireContext(), listContacts, ::itemClick, ::itemLongClick)

        return binding.root
    }

    private fun itemClick(i: Int) {
        uriToBeCalled = listContacts[i].number.convertToUri()
        requestPermission()
    }

    private fun itemLongClick(i: Int) {
        Toast.makeText(requireContext(),
            "Item long click on item $i function not implemented",
            Toast.LENGTH_SHORT).show()
    }

    private fun indirectPhoneCall(callUri: Uri) {
        //dangerous permission, requires to be requested at runtime
        val phoneCallIntent = Intent(Intent.ACTION_DIAL).also {
            it.data = callUri
        }
        startActivity(phoneCallIntent)
    }

    private fun directPhoneCall(callUri: Uri) {
        val phoneCallIntent = Intent(Intent.ACTION_CALL).also {
            it.data = callUri
        }
        startActivity(phoneCallIntent)
    }

    private val requestPermissionResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                //permission granted

                //direct phone call
                directPhoneCall(uriToBeCalled!!)
            } else {
                //permission denied

                //indirect phone call
                indirectPhoneCall(uriToBeCalled!!)
            }
        }

    private fun requestPermission() {
        when {
            ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED -> {
                //permission is already granted
                //if it is already granted, it won't fall to permission granted block in result launcher.
                //it'll have to be handled here:
                directPhoneCall(uriToBeCalled!!)
            }
            ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                Manifest.permission.CALL_PHONE) -> {
                //additional rationale should be displayed
                //show in ui why permission is required and go for request
                Snackbar.make(requireView(),
                    "Please give permission to make direct call",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK") {
                    requestPermissionResultLauncher.launch(Manifest.permission.CALL_PHONE)
                }.show()
            }
            else -> {
                //permission is yet to be asked
                requestPermissionResultLauncher.launch(Manifest.permission.CALL_PHONE)
            }
        }
    }

}