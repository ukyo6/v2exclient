package com.ukyoo.v2client.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ukyoo.v2client.entity.NodeModel
import io.reactivex.Flowable

@Dao
interface NodeModelDao {

    @Query("SELECT * from nodemodel")
    fun getAll(): Flowable<List<NodeModel>>

    @Query("SELECT * from nodemodel WHERE name = :name")
    fun queryNodesByName(name: String): Flowable<List<NodeModel>>

    @Insert
    fun insertAll(userEntities: List<NodeModel>)

}