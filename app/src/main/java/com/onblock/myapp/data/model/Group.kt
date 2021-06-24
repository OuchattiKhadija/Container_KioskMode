package com.onblock.myapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "group_table")
data class Group (
    @PrimaryKey(autoGenerate = false)
    var groupName: String,
    var description: String
)