package com.example.kys.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import com.example.kys.R
import com.example.kys.SignInActivity
import com.example.kys.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        val user = FirebaseAuth.getInstance()
        val intent = Intent(activity, SignInActivity::class.java)

        binding.apply {
            logoutButton.setOnClickListener{
                FirebaseAuth.getInstance().signOut()
                startActivity(intent)
            }
        }

        binding.apply {
            val userName = user.currentUser?.email.toString()
            val userName1: String = userName.substringBefore("@")

            Toast.makeText(activity, userName1, Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}