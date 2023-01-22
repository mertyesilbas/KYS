package com.example.kys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kys.databinding.ActivitySignUpBinding
import com.example.kys.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, SignInActivity::class.java)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.redirectSignInBtn.setOnClickListener {
            startActivity(intent)
        }
        // [START Sign Up]
        binding.signUpButton.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()


            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {
                    createAccount(email, pass)
                } else {
                    Toast.makeText(this, "Şifreler aynı değil!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Bütün alanları doldurunuz!", Toast.LENGTH_SHORT).show()
            }
        }
        // [END Sign Up]
    }

    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("EmailPassword", "createUserWithEmail:success")
                    val user = firebaseAuth.currentUser
                    val cuName = null
                    val cuEmail = user?.email  // CU stands for Current User
                    val cuUid = user!!.uid
                    createAccountOnDatabase(cuUid,cuName,cuEmail)
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("EmailPassword", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Kullanıcı Oluşturulamadı",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
        // [END create_user_with_email]
    }

    private fun createAccountOnDatabase(useruid: String, name: String?, email: String?) {
        database = Firebase.database.reference
        val user = User(name, email)
        database.child("Users").child(useruid).setValue(user)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}