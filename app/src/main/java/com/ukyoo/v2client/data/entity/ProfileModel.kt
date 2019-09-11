package com.ukyoo.v2client.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class ProfileModel(
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0,

    var username: String ="",
    var avatar: String ="",

    var nodes: Int = 0,
    var topics: Int = 0,
    var followings: Int = 0,
    var notifications: Int = 0
)