package com.example.kys.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
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
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.lang.Byte

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var storageReference: StorageReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)
        database = FirebaseDatabase.getInstance().getReference("Users")
        storageReference = Firebase.storage.reference


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
        binding.profileName.text = user.displayName.toString()

        setProfileImage(uid)

        binding.profileMail1.text = user.email.toString()

        binding.profileUpdate.setOnClickListener {
            EditProfileDialog(
                onSubmitClickListener = { username, photoUrl ->
                    // 1. Upload Photo to Storage
                    uploadPhotoToStorage(uid, photoUrl)
                    // Updating Firebase Auth
                    val storageRefUri = "gs://bucket/images/${uid}.jpg"
                    val profileUpdates = userProfileChangeRequest {
                        displayName = username
                        photoUri = Uri.parse(storageRefUri)
                    }

                    user.updateProfile(profileUpdates)
                    // Updating Firebase Database
                    updateProfile(uid, username, email, storageRefUri)
                    Toast.makeText(requireContext(), "Başarıyla Güncellendi", Toast.LENGTH_SHORT)
                        .show()
                    binding.profileName.text = user.displayName.toString()

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


    private fun setProfileImage(userUid: String) {
        storageReference = Firebase.storage.reference
        val imageStorageRef = storageReference.child("images/$userUid.jpg")
        imageStorageRef.getBytes(1024*1024*1024).addOnSuccessListener {
            val imageBitmap = BitmapFactory.decodeByteArray(it, 0 ,it.size)
            binding.profileImageView.setImageBitmap(imageBitmap)
        }
    }

    private fun updateProfile(
        userId: String?,
        username: String?,
        email: String?,
        photoUrl: String?
    ) {
        val user = User(username, email, photoUrl)
        database.child(userId!!).setValue(user)
    }

    private fun uploadPhotoToStorage(
        userUid: String,
        photoUri: String
    ) {
        storageReference = Firebase.storage.reference

        val fileUri = Uri.parse(photoUri)
        if (fileUri.lastPathSegment != null) {
            val strgRef = storageReference.child("images/$userUid.jpg")
            Log.d("UPLOAD PHOTO", strgRef.toString())
            strgRef.putFile(fileUri)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}