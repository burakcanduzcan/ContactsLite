package com.burakcanduzcan.contactslite.ui.contacts

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
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
import com.burakcanduzcan.contactslite.databinding.DialpadBinding
import com.burakcanduzcan.contactslite.databinding.FragmentContactsBinding
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
                ::addToQuickCallClick,
                ::addToGroupClick,
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

    private fun addToQuickCallClick(contact: Contact) {
        showQuickCallPopup(contact)
    }

    private fun addToGroupClick(contact: Contact) {
        /*
        todo
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

    @Suppress("DEPRECATION")
    private fun showQuickCallPopup(contact: Contact) {
        val b =
            DialpadBinding.bind(requireActivity().layoutInflater.inflate(R.layout.dialpad, null))
        val popupQuickCall = PopupWindow(
            b.root,
            requireActivity().windowManager.defaultDisplay.width,
            requireActivity().windowManager.defaultDisplay.height - 100)
        popupQuickCall.animationStyle = R.style.popup_window_animation
        popupQuickCall.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        popupQuickCall.isFocusable = true
        popupQuickCall.showAtLocation(b.root, Gravity.CENTER, 0, 0)

        b.ibBack.setOnClickListener {
            popupQuickCall.dismiss()
        }

        val buttons =
            arrayOf(b.btn0, b.btn1, b.btn2, b.btn3, b.btn4, b.btn5, b.btn6, b.btn7, b.btn8, b.btn9)
        val textViews = arrayOf(b.tvBtn0,
            b.tvBtn1,
            b.tvBtn2,
            b.tvBtn3,
            b.tvBtn4,
            b.tvBtn5,
            b.tvBtn6,
            b.tvBtn7,
            b.tvBtn8,
            b.tvBtn9)

        //setting existing quick calls and setOnClick listeners for adding/overriding quick calls

        //Save to sharedPreferences as "quickCall${i}" where i is number on dial pad
        //Save to database on "assignedQuickCallNumber" field
        for (i in 0..9) {
            //read from sharedPreferences to decide whether quick calls are  already set or not
            val value = pref.getInt("quickCall${i}", -1)


            //if value isn't -1, then this quickCall is already set.
            //we'll get it's value from the database
            if (value != -1) {
                val readContact: Contact = viewModel.getContactFromId(value)
                textViews[i].text = readContact.name
                Timber.i("DialPad[${i}] is already set with $value")

                //setOnClick action for saving quick call on this number
                //this will first clear the previous one
                buttons[i].setOnClickListener {
                    MaterialAlertDialogBuilder(requireContext())
                        .setMessage("Are you sure you want to assign this contact to number $i? This will remove the existing contact from quick call.")
                        .setPositiveButton("Yes") { _, _ ->
                            //remove old one from sharedPreferences by placing -1 in it
                            pref.edit().putInt("quickCall${i}", -1).commit()
                            Timber.i("SharedPreferences of QuickCall${i} is removed from contact: ${readContact.name} with id of ${contact.id}")
                            //set old contact's "assignedQuickCallNumber" field to "none"
                            viewModel.updateContactQuickCallField(readContact, "none")
                            Timber.i("Removed assignedQuickCallNumber field of contact: ${readContact.name}")
                            //save new contact id on sharedPreferences
                            pref.edit().putInt("quickCall${i}", contact.id).commit()
                            Timber.i("SharedPreferences of QuickCall${i} is assigned to contact: ${contact.name} with id of ${contact.id}")
                            //update new contact to note that it is assigned to a quick call
                            viewModel.updateContactQuickCallField(contact, i.toString())
                            Timber.i("Updated assignedQuickCallNumber field of contact: ${contact.name} with $i")
                            popupQuickCall.dismiss()
                        }
                        .setNegativeButton("No", null)
                        .show()
                }
            }
            //if value = -1, this quickCall is not set and the indicator will be set to show "empty"
            else {
                textViews[i].text = "empty"
                Timber.i("DialPad[${i}] is empty")

                //setOnClick action for saving quick call on this number
                buttons[i].setOnClickListener {
                    MaterialAlertDialogBuilder(requireContext())
                        .setMessage("Are you sure you want to assign this contact to number $i?")
                        .setPositiveButton("Yes") { _, _ ->
                            //save contact id on sharedPreferences
                            //update contact to note that it is assigned to a quick call
                            pref.edit().putInt("quickCall${i}", contact.id).commit()
                            Timber.i("SharedPreferences of QuickCall${i} is assigned to contact: ${contact.name} with id of ${contact.id}")
                            viewModel.updateContactQuickCallField(contact,
                                quickCallField = i.toString())
                            Timber.i("Updated assignedQuickCallNumber field of contact: ${contact.name} with $i")
                            popupQuickCall.dismiss()
                        }
                        .setNegativeButton("No", null)
                        .show()
                }
            }
        }
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
