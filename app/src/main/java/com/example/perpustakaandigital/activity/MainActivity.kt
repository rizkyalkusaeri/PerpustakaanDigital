package com.example.perpustakaandigital.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.perpustakaandigital.R
import com.example.perpustakaandigital.fragment.AccountFragment
import com.example.perpustakaandigital.fragment.HomeFragment
import com.example.perpustakaandigital.fragment.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    var fragment: Fragment? = HomeFragment()
    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> fragment = HomeFragment()
                R.id.navigation_search -> fragment = SearchFragment()
                R.id.navigation_account -> fragment = AccountFragment()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_layout, fragment!!)
                .commit()
            true
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navigationView =
            findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        navigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_layout, fragment!!)
                .commit()
        } else {
            fragment = supportFragmentManager.getFragment(
                savedInstanceState,
                ConstantUtils.KEY_FRAGMENT
            )
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_layout, fragment!!)
                .commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        supportFragmentManager.putFragment(outState, ConstantUtils.KEY_FRAGMENT, fragment!!)
        super.onSaveInstanceState(outState)
    }
}