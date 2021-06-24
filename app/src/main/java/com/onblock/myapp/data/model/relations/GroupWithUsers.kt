package com.onblock.myapp.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.onblock.myapp.data.model.Group
import com.onblock.myapp.data.model.User

data class GroupWithUsers(
    @Embedded  val group: Group,
    @Relation(
        parentColumn = "groupName",
        entityColumn = "groupName"
    )
    val users: List<User>
)
