package com.ukyoo.v2client.data.db

import androidx.room.*
import com.ukyoo.v2client.data.entity.NodeModel
import io.reactivex.Flowable


@Dao
interface NodeModelDao {

    @Query("SELECT * from nodemodel")
    fun getAll(): Flowable<List<NodeModel>>

    @Query("SELECT * from nodemodel WHERE name LIKE :name or title LIKE :name or titleAlternative like :name")
    fun queryNodesByName(name: String): Flowable<List<NodeModel>>

    @Insert
    fun insertAll(userEntities: List<NodeModel>)

    @Query("DELETE FROM nodemodel")
    fun deleteAll()
}