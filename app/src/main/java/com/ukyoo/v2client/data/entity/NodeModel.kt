package com.ukyoo.v2client.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "nodemodel")
data class NodeModel(

    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0,

    var id: Int = 0,
    var name: String? = null,
    var title: String? = null,
    var titleAlternative: String? = null,
    var url: String? = null,
    var topics: Int = 0,
    var header: String? = null,
    var footer: String? = null,

    var isCollected: Boolean = false

) : Parcelable