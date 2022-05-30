package com.example.kys.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.kys.ChangeProfileActivity
import com.example.kys.DBHelper
import com.example.kys.R
import com.example.kys.SignInActivity
import com.example.kys.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("Recycle")
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
            val db = DBHelper(requireActivity(),null)

            val dbQuery = db.readableDatabase
            val userUid = user.currentUser?.uid.toString()
            val query = "SELECT username FROM profile WHERE profile.user_uid = " + "'"+ userUid + "'"

            val getProfileName = dbQuery.rawQuery(query , null)
            getProfileName.moveToFirst()

            profileImageView.setImageResource(R.mipmap.ic_profile_photo_foreground)
            profileName.text = getProfileName.getString(0)

//            Toast.makeText(activity, getProfileName.getString(0), Toast.LENGTH_SHORT).show()
        }

        binding.apply {
            profileChangeName.setOnClickListener{
               val intent = Intent(activity, ChangeProfileActivity::class.java)
                startActivity(intent)
            }

            profileMail1.text = user.currentUser?.email.toString()
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}