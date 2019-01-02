package com.ukyoo.v2client.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ukyoo.v2client.App
import com.ukyoo.v2client.entity.NodeModel

/**
 * 创建数据库和Dao
 */
@Database(entities = [NodeModel::class], version = 2)
abstract class AppDataBase : RoomDatabase() {
    abstract fun nodeModelDao(): NodeModelDao

    companion object {
        @Synchronized
        fun getDataBase(): AppDataBase {
            return Room.databaseBuilder(
                App.instance(),
                AppDataBase::class.java, "database-v2ex"
            ).build()
        }
    }
}