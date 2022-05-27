package com.burakcanduzcan.contactslite.ui.dialPad

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.burakcanduzcan.contactslite.R
import com.burakcanduzcan.contactslite.databinding.FragmentDialpadBinding
import com.google.android.material.snackbar.Snackbar

class DialPadFragment : Fragment(), AdapterView.OnItemSelectedListener,
    AdapterView.OnItemClickListener {
    private lateinit var binding: FragmentDialpadBinding
    private val viewModel: DialPadViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDialpadBinding.inflate(inflater)

        setSpinnerAdapter()

        viewModel.enteredPhoneNumber.observe(this.viewLifecycleOwner) { phoneNumber ->
            binding.tvPhoneNumber.text = phoneNumber
        }

        initializeButtons()

        return binding.root
    }

    private fun setSpinnerAdapter() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.country_phone_codes,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spCountryCode.adapter = adapter
            binding.spCountryCode.onItemSelectedListener = this
        }
    }

    private fun initializeButtons() {
        binding.btnBackspace.setOnClickListener {
            viewModel.removeLastDigit()
        }
        binding.btn1.setOnClickListener {
            viewModel.addDigit('1')
        }
        binding.btn2.setOnClickListener {
            viewModel.addDigit('2')
        }
        binding.btn3.setOnClickListener {
            viewModel.addDigit('3')
        }
        binding.btn4.setOnClickListener {
            viewModel.addDigit('4')
        }
        binding.btn5.setOnClickListener {
            viewModel.addDigit('5')
        }
        binding.btn6.setOnClickListener {
            viewModel.addDigit('6')
        }
        binding.btn7.setOnClickListener {
            viewModel.addDigit('7')
        }
        binding.btn8.setOnClickListener {
            viewModel.addDigit('8')
        }
        binding.btn9.setOnClickListener {
            viewModel.addDigit('9')
        }
        binding.btn0.setOnClickListener {
            viewModel.addDigit('0')
        }
        binding.btnCall.setOnClickListener {
            viewModel.setUriToBeCalled()
            requestPermission()
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        Toast.makeText(requireContext(),
            p0!!.getItemAtPosition(p2).toString() + " selected",
            Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
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
    }

    private fun directPhoneCall(callUri: Uri) {
        //dangerous permission, requires to be requested at runtime
        val phoneCallIntent = Intent(Intent.ACTION_CALL).also {
            it.data = callUri
        }
        startActivity(phoneCallIntent)
    }
}