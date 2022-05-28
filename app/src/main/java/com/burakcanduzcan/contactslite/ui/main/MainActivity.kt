package com.burakcanduzcan.contactslite.ui.main

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.burakcanduzcan.contactslite.R
import com.burakcanduzcan.contactslite.databinding.ActivityMainBinding
import com.burakcanduzcan.contactslite.databinding.PopupSelectCountryBinding
import com.burakcanduzcan.contactslite.ui.contacts.ContactsFragment
import com.burakcanduzcan.contactslite.ui.dialPad.DialPadFragment
import com.burakcanduzcan.contactslite.ui.groups.GroupsFragment
import com.burakcanduzcan.contactslite.ui.settings.SettingsFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        pref = getSharedPreferences(packageName, MODE_PRIVATE)
        setContentView(binding.root)

        setViewPagerAdapter()
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_dialpad_24, null)
                }
                1 -> {
                    tab.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_person_24, null)
                }
                2 -> {
                    tab.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_groups_24, null)
                }
                3 -> {
                    tab.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_settings_24, null)
                }
            }
        }.attach()

        if (pref.getString("defaultCountry", "DEFAULT") == "DEFAULT") {
            showDefaultCountrySelectionDialog()
        }
    }

    private fun setViewPagerAdapter() {
        val adapter = MainViewPagerAdapter(this)
        adapter.addFragment(DialPadFragment())
        adapter.addFragment(ContactsFragment())
        adapter.addFragment(GroupsFragment())
        adapter.addFragment(SettingsFragment())
        binding.viewPager.adapter = adapter
    }

    fun changeFabAction(newFunction: () -> Unit) {
        binding.fab.setOnClickListener {
            newFunction()
            //yet to be used
        }
    }

    private fun showDefaultCountrySelectionDialog() {
        val builder = AlertDialog.Builder(this)
        val bindingAlertDialog = PopupSelectCountryBinding.inflate(LayoutInflater.from(this))
        builder.setView(bindingAlertDialog.root)
        builder.setTitle(R.string.please_confirm_your_country)
        builder.setPositiveButton(R.string.confirm) { _, _ ->
            pref.edit()
                .putString("defaultCountry", bindingAlertDialog.countryCodePicker.selectedCountryNameCode)
                .apply()
        }
        builder.setCancelable(false)


        builder.show()
    }
}