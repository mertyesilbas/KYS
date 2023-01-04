package com.example.kys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kys.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, SignInActivity::class.java)
        val user = FirebaseAuth.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        binding.textView.setOnClickListener {
            startActivity(intent)
        }
        binding.signUpButton.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Başarıyla Kayıt Oldunuz!", Toast.LENGTH_SHORT).show()
                            // Creating User Profile
                            val db = DBHelper(this,null)

                            val userName = user.currentUser?.email.toString()
                            val userName1: String = userName.substringBefore("@")

                            val profilePhoto = "drawable://" + R.mipmap.ic_profile_photo_foreground

                            val sdfDate = SimpleDateFormat("dd/M/yyyy", Locale("tr"))
                            val sdfTime = SimpleDateFormat("HH:mm:ss", Locale("tr"))
                            val createDate = sdfDate.format(Date())
                            val createTime = sdfTime.format(Date())
                            val userUid = user.currentUser?.uid.toString()

                            db.addProfile(userName1, profilePhoto, createDate, createTime, userUid)

                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Şifreler aynı değil!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Bütün alanları doldurunuz!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}