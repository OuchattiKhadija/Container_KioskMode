package com.onblock.myapp.data.model

import android.provider.ContactsContract
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = false)
    var cin: String,
    var firstName: String,
    var lastName: String,
    var function: String,
    var userName: String,
    var password: String,
    var groupName: String
)