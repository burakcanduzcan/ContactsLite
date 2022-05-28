package com.burakcanduzcan.contactslite.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.burakcanduzcan.contactslite.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var pref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        pref = requireContext().getSharedPreferences(requireActivity().packageName, AppCompatActivity.MODE_PRIVATE)

        setupExistingSettings()

        return binding.root
    }

    private fun setupExistingSettings() {
        val settings1: String = pref.getString("defaultCountry", "DEFAULT").toString()
        binding.ccpDefaultCountry.setCountryForNameCode(settings1)
        binding.btnSetting1.setOnClickListener {
            pref.edit().putString("defaultCountry", binding.ccpDefaultCountry.selectedCountryNameCode).apply()
            binding.btnSetting1.isEnabled = false
        }
        binding.ccpDefaultCountry.setOnCountryChangeListener {
            binding.btnSetting1.isEnabled = (settings1 != binding.ccpDefaultCountry.selectedCountryNameCode)
        }
    }
}