package com.burakcanduzcan.contactslite.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.burakcanduzcan.contactslite.R
import com.burakcanduzcan.contactslite.databinding.ActivityMainBinding
import com.burakcanduzcan.contactslite.ui.contacts.ContactsFragment
import com.burakcanduzcan.contactslite.ui.dialPad.DialPadFragment
import com.burakcanduzcan.contactslite.ui.groups.GroupsFragment
import com.burakcanduzcan.contactslite.ui.settings.SettingsFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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
        }
    }
}