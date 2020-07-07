package com.example.perpustakaandigital.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.perpustakaandigital.R
import com.example.perpustakaandigital.fragment.AccountFragment
import com.example.perpustakaandigital.fragment.PeminjamanFragment
import com.example.perpustakaandigital.fragment.SkripsiFragment
import com.example.perpustakaandigital.storage.SharedPrefManager
import com.example.perpustakaandigital.utils.ConstantUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var fragment: Fragment? = SkripsiFragment()

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> fragment = SkripsiFragment()
                R.id.navigation_account -> fragment = AccountFragment()
                R.id.navigation_buku -> fragment = PeminjamanFragment()
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.container_layout, fragment!!)
                .commit()
            true
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_toolbar)

        val navigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main,menu)
        return true
    }


    override fun onSaveInstanceState(outState: Bundle) {
        supportFragmentManager.putFragment(outState, ConstantUtils.KEY_FRAGMENT, fragment!!)
        super.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()

        if (!SharedPrefManager.getInstance(this).isLoggedIn) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

}