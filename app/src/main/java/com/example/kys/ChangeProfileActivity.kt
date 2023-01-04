package com.example.kys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_change_profile.*

class ChangeProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_profile)

        val user = FirebaseAuth.getInstance()
        val intent = Intent(this, MainActivity::class.java)

        newUsernameButton.setOnClickListener {
            val newUsername = newUsername.editText?.text.toString()
            val db = DBHelper(this, null)

            val dbQuery = db.writableDatabase
            val userUid = user.currentUser?.uid.toString()
            val query =
                "UPDATE profile SET username = " + "'" + newUsername + "'" + " WHERE profile.user_uid = " + "'" + userUid + "'"

            dbQuery.execSQL(query)
            startActivity(intent)

        }
    }
}