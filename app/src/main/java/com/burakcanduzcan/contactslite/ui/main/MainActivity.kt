package com.burakcanduzcan.contactslite.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.burakcanduzcan.contactslite.R
import com.burakcanduzcan.contactslite.databinding.ActivityMainBinding
import com.burakcanduzcan.contactslite.ui.contacts.ContactsFragment
import com.burakcanduzcan.contactslite.ui.dialPad.DialPadFragment
import com.burakcanduzcan.contactslite.ui.groups.GroupsFragment
import com.burakcanduzcan.contactslite.ui.settings.SettingsFragment
import com.burakcanduzcan.contactslite.utils.getDrawableCompat
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setViewPagerAdapter()
        initTabLayout()
    }

    private fun setViewPagerAdapter() {
        val adapter = MainViewPagerAdapter(this)
        adapter.addFragment(DialPadFragment())
        adapter.addFragment(ContactsFragment())
        adapter.addFragment(GroupsFragment())
        adapter.addFragment(SettingsFragment())
        binding.viewPager.adapter = adapter
    }

    private fun initTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.icon = getTabIconByPosition(position)
        }.attach()
    }

    private fun getTabIconByPosition(position: Int) = when (position) {
        0 -> R.drawable.ic_baseline_dialpad_24
        1 -> R.drawable.ic_baseline_person_24
        2 -> R.drawable.ic_baseline_groups_24
        3 -> R.drawable.ic_baseline_settings_24
        else -> null
    }?.let(::getDrawableCompat)

    fun setFabVisibility(visible: Boolean) {
        if (visible) {
            binding.fab.visibility = View.VISIBLE
        } else {
            binding.fab.visibility = View.INVISIBLE
        }
    }

    fun changeFabActionFromFragments(newFabAction: () -> Unit) {
        binding.fab.setOnClickListener {
            newFabAction()
        }
    }

    fun changeCurrentViewPagerPage(pageIndex: Int) {
        if (pageIndex in 0..3) {
            binding.viewPager.currentItem = pageIndex
        }
    }
}