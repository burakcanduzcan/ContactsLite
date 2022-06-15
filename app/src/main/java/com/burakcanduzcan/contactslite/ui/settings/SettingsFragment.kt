package com.burakcanduzcan.contactslite.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.burakcanduzcan.contactslite.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModels()
    //private lateinit var pref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        //pref = requireContext().getSharedPreferences(requireContext().getString(R.string.preference_file_key), AppCompatActivity.MODE_PRIVATE)

        setupExistingSettings()

        return binding.root
    }

    private fun setupExistingSettings() {
        // Settings #1
        //val settings1: String = pref.getString("defaultCountry", "DEFAULT").toString()
        binding.ccpDefaultCountry.setCountryForNameCode(viewModel.getDefaultCountryNameCode())
        binding.btnSetting1.setOnClickListener {
            viewModel.setDefaultCountry(binding.ccpDefaultCountry.selectedCountryNameCode)
            binding.btnSetting1.isEnabled = false
        }
        binding.ccpDefaultCountry.setOnCountryChangeListener {
            binding.btnSetting1.isEnabled = (viewModel.getDefaultCountryNameCode() != binding.ccpDefaultCountry.selectedCountryNameCode)
        }

        // ...
    }
}