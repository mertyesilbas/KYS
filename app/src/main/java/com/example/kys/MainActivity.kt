package com.example.kys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.kys.fragments.CreateConfFragment
import com.example.kys.fragments.HomeFragment
import com.example.kys.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    // [START declare_auth]
    private lateinit var firebaseAuth: FirebaseAuth
    // [END declare_auth]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseAuth = FirebaseAuth.getInstance()

        val fragmentManager: FragmentManager = supportFragmentManager

        val homeFragment = HomeFragment()
        val createConfFragment = CreateConfFragment()
        val profileFragment = ProfileFragment()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            lateinit var fragment: Fragment
            when (item.itemId) {
                R.id.home -> fragment = homeFragment
                R.id.createConf -> fragment = createConfFragment
                R.id.profile -> fragment = profileFragment
            }
            fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit()
            true
        }
        bottomNavigationView.selectedItemId = R.id.home

        if (intent != null && intent.getStringExtra("Mode") == "E") {
            val args = Bundle()
            args.putString("Mode", "E")
            args.putString("Id", intent.getIntExtra("Id", 0).toString())
            val fragment = CreateConfFragment()
            fragment.arguments = args
            fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit()
        }
    }

    // [START on_start_check_user]
    override fun onStart() {
        super.onStart()
        validateCurrentUser()
    }

    fun validateCurrentUser(){
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }
    // [END on_start_check_user]
    override fun onDestroy() {
        super.onDestroy()
        val user = FirebaseAuth.getInstance()
        user.signOut()
    }

}