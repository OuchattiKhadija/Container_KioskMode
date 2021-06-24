package com.onblock.myapp.data.model.relations

import androidx.room.Entity

@Entity(primaryKeys = ["packageName","groupName"])
data class AppGroupCrossRef(
    var packageName: String,
    var groupName: String,
    var isAlowed : Boolean
)