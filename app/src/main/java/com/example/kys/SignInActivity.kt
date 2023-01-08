package com.example.kys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.kys.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // [START initialize_auth]
        // Initialize Firebase Auth
        firebaseAuth = Firebase.auth
        // [END initialize_auth]

        // Logging in with normal entrance
        // [START Logging In Normal]
        binding.loginButton.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {

//                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
//                    if (it.isSuccessful) {
//                        validateCurrentUser()
//                    } else {
//                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
//                    }
//                }
                signIn(email,pass)
            } else {
                Toast.makeText(this, "Bütün alanları doldurunuz!", Toast.LENGTH_SHORT).show()
            }
        }
        // [END Logging In Normal]

        // Logging in with google sign in entrance
//        oneTapClient = Identity.getSignInClient(this)
//        signInRequest = BeginSignInRequest.Builder()
//            .setGoogleIdTokenRequestOptions(
//                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                    .setSupported(true)
//                    .setServerClientId(getString(R.string.your_web_client_id))
//                    .setFilterByAuthorizedAccounts(true)
//                    .build()
//            )
//            .setAutoSelectEnabled(true)
//            .build()

        binding.redirectSignUpBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        updateUI(firebaseAuth.currentUser)
    }

//    fun validateCurrentUser(){
//        val currentUser = firebaseAuth.currentUser
//        if (currentUser != null) {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//        }
//    }

    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("EmailPassword", "signInWithEmail:success")
                    val user = firebaseAuth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("EmailPassword", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Giriş Yapılamadı",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
        // [END sign_in_with_email]
    }

    private fun updateUI(user: FirebaseUser?) {
        if(user != null){
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}