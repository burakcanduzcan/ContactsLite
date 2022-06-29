package com.burakcanduzcan.contactslite.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.burakcanduzcan.contactslite.databinding.FragmentSettingsBinding
import com.burakcanduzcan.contactslite.ui.main.MainActivity

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).setFabVisibility(false)
        setupExistingSettings()
    }

    private fun setupExistingSettings() {
        // Settings #1 - Default country
        binding.ccpDefaultCountry.setCountryForNameCode(viewModel.getDefaultCountryNameCode())
        binding.btnSetting1.setOnClickListener {
            viewModel.setDefaultCountry(binding.ccpDefaultCountry.selectedCountryNameCode)
            binding.btnSetting1.isEnabled = false
        }
        binding.ccpDefaultCountry.setOnCountryChangeListener {
            binding.btnSetting1.isEnabled =
                (viewModel.getDefaultCountryNameCode() != binding.ccpDefaultCountry.selectedCountryNameCode)
        }


        // Settings #2 - Phone number validator
        binding.cBoxSetting2.isChecked = viewModel.isPhoneNumberValidatorEnabled()
        binding.cBoxSetting2.setOnCheckedChangeListener { _, _ ->
            viewModel.changePhoneNumberValidatorSettings()
        }

        // ...
    }
}