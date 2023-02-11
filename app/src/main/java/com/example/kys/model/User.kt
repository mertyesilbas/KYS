package com.example.kys.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

// [START rtdb_user_class]
@IgnoreExtraProperties
data class User(
    val uid: String? = "",
    val username: String? = "",
    val email: String? = "",
    val photoUrl: String? = ""
) {

    @Exclude
    fun toMap(): Map<String,Any?>{
        return mapOf(
            "uid" to uid,
            "username" to username,
            "email" to email,
            "photoUrl" to photoUrl
        )
    }
}
// [END rtdb_user_class]