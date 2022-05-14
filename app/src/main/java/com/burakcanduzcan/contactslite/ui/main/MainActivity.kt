package com.burakcanduzcan.contactslite.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.burakcanduzcan.contactslite.R
import com.burakcanduzcan.contactslite.databinding.ActivityMainBinding
import com.burakcanduzcan.contactslite.databinding.ItemTabBinding
import com.burakcanduzcan.contactslite.ui.contacts.ContactsFragment
import com.burakcanduzcan.contactslite.ui.dialPad.DialPadFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setViewPagerAdapter()
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ ->
        }.attach()
        configureTabs()
    }

    private fun setViewPagerAdapter() {
        val adapter = MainViewPagerAdapter(this)
        adapter.addFragment(DialPadFragment())
        adapter.addFragment(ContactsFragment())
        //...
        binding.viewPager.adapter = adapter
    }

    private fun configureTabs() {
        var tab = ItemTabBinding.inflate(layoutInflater)
        tab.ivLogo.setImageResource(R.drawable.ic_baseline_dialpad_24)
        binding.tabLayout.getTabAt(0)!!.customView = tab.root

        tab = ItemTabBinding.inflate(layoutInflater)
        tab.ivLogo.setImageResource(R.drawable.ic_baseline_person_24)
        binding.tabLayout.getTabAt(1)!!.customView = tab.root
        //...
    }

    fun changeFabAction(newFunction: () -> Unit) {
        binding.fab.setOnClickListener {
            newFunction()
        }
    }

}