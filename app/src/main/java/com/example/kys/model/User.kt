package com.example.kys.model

import com.google.firebase.database.IgnoreExtraProperties

// [START rtdb_user_class]
@IgnoreExtraProperties
data class User(
    val username: String? = null,
    val email: String? = null
)
// [END rtdb_user_class]