package com.burakcanduzcan.contactslite.ui.contacts

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.burakcanduzcan.contactslite.ContactApplication
import com.burakcanduzcan.contactslite.R
import com.burakcanduzcan.contactslite.data.entity.Contact
import com.burakcanduzcan.contactslite.databinding.FragmentContactsBinding
import com.burakcanduzcan.contactslite.databinding.FragmentDialpadBinding
import com.burakcanduzcan.contactslite.databinding.PopupUserBinding
import com.burakcanduzcan.contactslite.ui.main.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

class ContactsFragment : Fragment() {
    private lateinit var binding: FragmentContactsBinding
    private lateinit var pref: SharedPreferences

    private val viewModel: ContactsViewModel by activityViewModels {
        ContactsViewModelFactory(
            (activity?.application as ContactApplication).database.contactDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentContactsBinding.inflate(layoutInflater)
        pref =
            requireContext().getSharedPreferences(requireContext().getString(R.string.preference_file_key),
                Context.MODE_PRIVATE)

        binding.rvContacts.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val adapter =
            ContactListAdapter(
                ::itemClick,
                ::editClick,
                ::starClick,
                ::addToQuickCallClick,
                ::deleteClick)
        binding.rvContacts.adapter = adapter

        viewModel.allContacts.observe(this.viewLifecycleOwner) { contacts ->
            contacts.let {
                adapter.submitList(it)
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).setFabVisibility(true)
        (requireActivity() as MainActivity).changeFabActionFromFragments() {
            showAddOrEditUserDialog(isActionInsertion = true)
        }
    }

    private fun itemClick(contact: Contact) {
        viewModel.setUriToBeCalled(contact)
        Timber.i("Calling ${contact.name} from contact list")
        requestPermission()
    }

    private fun editClick(contact: Contact) {
        showAddOrEditUserDialog(isActionInsertion = false, contact)
    }

    private fun starClick(contact: Contact) {
        val bindingQuickcallAdd = FragmentDialpadBinding.inflate(LayoutInflater.from(requireContext()))
        AlertDialog.Builder(requireContext())
            .setView(bindingQuickcallAdd.root)
            .show()
    }

    private fun addToQuickCallClick(contact: Contact) {
        /*
        todo: not implemented
         */
    }

    private fun deleteClick(contact: Contact) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_contact))
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                viewModel.deleteContact(contact)
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ ->
            }
            .show()
    }

    private val requestPermissionResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                //permission granted

                //direct phone call
                directPhoneCall(viewModel.uriToBeCalled!!)
            } else {
                //permission denied

                //indirect phone call
                indirectPhoneCall(viewModel.uriToBeCalled!!)
            }
        }

    private fun requestPermission() {
        when {
            ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED -> {
                //permission is already granted:
                //if it is already granted, it won't fall to permission granted block in result launcher;
                //it'll have to be handled here.
                directPhoneCall(viewModel.uriToBeCalled!!)
            }
            ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                Manifest.permission.CALL_PHONE) -> {
                //additional rationale should be displayed:
                //show in ui why permission is required and go for request.
                Snackbar.make(requireView(),
                    "Please give permission to make direct call",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK") {
                    requestPermissionResultLauncher.launch(Manifest.permission.CALL_PHONE)
                }.show()
            }
            else -> {
                //permission is yet to be asked:
                requestPermissionResultLauncher.launch(Manifest.permission.CALL_PHONE)
            }
        }
    }

    private fun indirectPhoneCall(callUri: Uri) {
        val phoneCallIntent = Intent(Intent.ACTION_DIAL).also {
            it.data = callUri
        }
        startActivity(phoneCallIntent)
        Timber.i("An indirect phone call to $callUri")
    }

    private fun directPhoneCall(callUri: Uri) {
        //dangerous permission, requires to be requested at runtime
        val phoneCallIntent = Intent(Intent.ACTION_CALL).also {
            it.data = callUri
        }
        startActivity(phoneCallIntent)
        Timber.i("A direct phone call to $callUri")
    }

    private fun showAddOrEditUserDialog(
        isActionInsertion: Boolean,
        existingContact: Contact? = null,
    ) {
        val bindingUserDialog = PopupUserBinding.inflate(LayoutInflater.from(requireContext()))
        bindingUserDialog.ccp.registerCarrierNumberEditText(bindingUserDialog.etPhoneNumber)

        val adb = AlertDialog.Builder(requireContext())
            .setView(bindingUserDialog.root)

        if (isActionInsertion) {
            //insert
            bindingUserDialog.ccp.setCountryForNameCode(pref.getString("defaultCountry", "TR"))
            adb.setTitle(getString(R.string.add_new_contact))
            adb.setPositiveButton(R.string.add) { _, _ ->
                //check whether name field is empty
                if (bindingUserDialog.etName.text.toString().isEmpty()) {
                    Snackbar.make(requireView(),
                        R.string.entered_contact_name_cannot_be_blank,
                        Snackbar.LENGTH_SHORT)
                        .setAction(R.string.ok) {
                        }
                        .show()
                } else {
                    //check whether phone number validator is enabled
                    if (pref.getBoolean("phoneNumberValidator", false)) {
                        if (bindingUserDialog.ccp.isValidFullNumber) {
                            viewModel.addNewContact(
                                bindingUserDialog.etName.text.toString(),
                                bindingUserDialog.ccp.selectedCountryCode,
                                bindingUserDialog.etPhoneNumber.text.toString()
                            )
                        } else {
                            Snackbar.make(requireView(),
                                R.string.entered_phone_number_was_not_valid,
                                Snackbar.LENGTH_SHORT)
                                .setAction(R.string.ok) {
                                }
                                .addAction(R.layout.snackbar_extra_button,
                                    getString(R.string.settings)
                                ) {
                                    (requireActivity() as MainActivity).changeCurrentViewPagerPage(3)
                                }
                                .show()
                        }
                    } else {
                        viewModel.addNewContact(
                            bindingUserDialog.etName.text.toString(),
                            bindingUserDialog.ccp.selectedCountryCode,
                            bindingUserDialog.etPhoneNumber.text.toString()
                        )
                    }
                }
            }
        } else {
            //edit
            bindingUserDialog.ccp.setCountryForPhoneCode(Integer.valueOf(existingContact!!.countryCode))
            bindingUserDialog.etName.setText(existingContact.name)
            bindingUserDialog.etPhoneNumber.setText(existingContact.number)
            adb.setTitle(getString(R.string.edit_contact))
            adb.setPositiveButton(R.string.update) { _, _ ->
                //check whether name field is empty
                if (bindingUserDialog.etName.text.toString().isEmpty()) {
                    Snackbar.make(requireView(),
                        R.string.entered_contact_name_cannot_be_blank,
                        Snackbar.LENGTH_SHORT)
                        .setAction(R.string.ok) {
                        }
                        .show()
                } else {
                    //check whether phone number validator is enabled
                    if (pref.getBoolean("phoneNumberValidator", false)) {
                        if (bindingUserDialog.ccp.isValidFullNumber) {
                            viewModel.updateContact(
                                existingContact,
                                bindingUserDialog.etName.text.toString(),
                                bindingUserDialog.ccp.selectedCountryCode,
                                bindingUserDialog.etPhoneNumber.text.toString()
                            )
                        } else {
                            Snackbar.make(requireView(),
                                R.string.entered_phone_number_was_not_valid,
                                Snackbar.LENGTH_SHORT)
                                .setAction(R.string.ok) {
                                }
                                .addAction(R.layout.snackbar_extra_button,
                                    getString(R.string.settings)
                                ) {
                                    (requireActivity() as MainActivity).changeCurrentViewPagerPage(3)
                                }
                                .show()
                        }
                    } else {
                        viewModel.updateContact(
                            existingContact,
                            bindingUserDialog.etName.text.toString(),
                            bindingUserDialog.ccp.selectedCountryCode,
                            bindingUserDialog.etPhoneNumber.text.toString()
                        )
                    }
                }
            }
        }

        adb.setNegativeButton(R.string.cancel, null)
        adb.show()
    }

    private fun Snackbar.addAction(
        @LayoutRes aLayoutId: Int,
        @StringRes aLabel: String,
        aListener: View.OnClickListener?,
    ): Snackbar {
        val button = LayoutInflater.from(view.context).inflate(aLayoutId, null) as Button
        view.findViewById<Button>(com.google.android.material.R.id.snackbar_action).let {
            button.layoutParams = it.layoutParams
            (button as? Button)?.setTextColor(it.textColors)
            (it.parent as? ViewGroup)?.addView(button)
        }
        button.text = aLabel
        button.setOnClickListener { this.dismiss(); aListener?.onClick(it) }
        return this
    }
}
