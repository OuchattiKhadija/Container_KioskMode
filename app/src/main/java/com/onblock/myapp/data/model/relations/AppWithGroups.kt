package com.onblock.myapp.data.model.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.onblock.myapp.data.model.AppInfo
import com.onblock.myapp.data.model.Group

data class AppWithGroups(
    @Embedded val appInfo: AppInfo,
    @Relation(
        parentColumn = "packageName",
        entityColumn = "groupName",
        associateBy = Junction(AppGroupCrossRef::class)
    )
    val groups: List<Group>
)