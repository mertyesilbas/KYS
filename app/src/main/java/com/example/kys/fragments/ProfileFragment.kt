package com.example.kys.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.kys.R
import com.example.kys.SignInActivity
import com.example.kys.databinding.FragmentProfileBinding
import com.example.kys.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

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

        setProfileImage(uid)


        Log.d("PHOTO URI", photoUrl.toString())

        binding.profileName.text = user.displayName.toString()

        binding.profileMail1.text = user.email.toString()

        binding.profileUpdate.setOnClickListener {
            EditProfileDialog(
                onSubmitClickListener = { username, photoUrl ->
                    Log.d("PHOTO LOCAL URL", photoUrl.toString())
                    // Updating Firebase Auth
                    val profileUpdates = userProfileChangeRequest {
                        displayName = username
                        photoUri = Uri.parse(photoUrl)
                    }

                    user.updateProfile(profileUpdates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(
                                    "UPDATE PROFILE",
                                    "User Profile Updated, New USERNAME: ${Firebase.auth.currentUser?.displayName}"
                                )
                            }
                        }
                    // Updating Firebase Database
                    Log.d("NEW USERNAME", username)
//                    val userModel = User(username, email)
//                    database.child(uid).setValue(userModel)
                    updateProfile(uid,username,email,photoUrl)
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


    private fun setProfileImage(userUid: String) {
        // Get Image Url From Database
        val photoListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val photoUrl = snapshot.getValue()
                Log.d("PHOTO URL", photoUrl.toString())
                if (photoUrl == null) { // If user doesn't have an image show default image
                    storageReference =
                        FirebaseStorage.getInstance()
                            .getReference("images/ic_profile_photo_round.png")
                    Log.d("STORAGE", storageReference.toString())
                    val localFile = File.createTempFile("image", "png")
                    storageReference.getFile(localFile).addOnSuccessListener {
                        binding.profileImageView.setImageBitmap(BitmapFactory.decodeFile(localFile.absolutePath))
                    }
                    binding.profileImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.mipmap.ic_profile_photo
                        )
                    )
                } else { // Show user's image

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }

        database.child(userUid).child("photoUrl").addValueEventListener(photoListener)
//        if (imageUrl.getValue() == null){
//            Log.d("PHOTO URL", imageUrl.getValue())
//        }
//        binding.profileImageView.setImageURI(photoUrl)
    }

    private fun updateProfile(userId: String?, username: String?, email: String?, photoUrl: String?){
//        val key = database.push().key
//        if (key == null){
//            Log.w("PUSH KEY", "Couldn't get push key for users")
//            return
//        }

        val user = User(userId,username,email,photoUrl)
//        val userValues = user.toMap()
//
//        val childUpdates = hashMapOf<String, Any>(
//            "$key" to userValues
//        )
//
//        database.updateChildren(childUpdates)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}