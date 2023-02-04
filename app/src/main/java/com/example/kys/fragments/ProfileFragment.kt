package com.example.kys.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kys.R
import com.example.kys.SignInActivity
import com.example.kys.databinding.FragmentProfileBinding
import com.example.kys.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)
        database = FirebaseDatabase.getInstance().getReference("Users")

        val intent = Intent(activity, SignInActivity::class.java)

        // [START access_user_info]
        val user = Firebase.auth.currentUser
        // Name, email address, and profile photo Url
        val name = user!!.displayName
        val email = user.email
        val photoUrl = user.photoUrl

        // Check if user's email is verified
        val emailVerified = user.isEmailVerified

        // The user's ID, unique to the Firebase project. Do NOT use this value to
        // authenticate with your backend server, if you have one. Use
        // FirebaseUser.getToken() instead.
        val uid = user.uid
        // [STOP access_user_info]


        binding.profileImageView.setImageResource(R.mipmap.ic_profile_photo_foreground)

        binding.profileName.text = user.displayName.toString()

        binding.profileMail1.text = user.email.toString()

        binding.profileUpdate.setOnClickListener {
            EditProfileDialog(
                onSubmitClickListener = { username ->
                    // Updating Firebase Auth
                    val profileUpdates = userProfileChangeRequest {
                        displayName = username
                    }

                    user!!.updateProfile(profileUpdates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(
                                    "UPDATE PROFILE",
                                    "User Profile Updated, New USERNAME: ${Firebase.auth.currentUser?.displayName}"
                                )
                            }
                        }
                    // Updating Firebase Database
                    val userModel = User(username, email)
                    database.child(uid).setValue(userModel)
                    Toast.makeText(requireContext(), "Başarıyla Güncellendi", Toast.LENGTH_SHORT)
                        .show()
                    requireActivity()
                        .supportFragmentManager.beginTransaction()
                        .replace(R.id.mainFrame, HomeFragment())
                        .commit()
                    requireActivity().bottom_navigation.selectedItemId = R.id.home
                }
            ).show(parentFragmentManager, "dialog")
        }

        binding.logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(intent)
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}