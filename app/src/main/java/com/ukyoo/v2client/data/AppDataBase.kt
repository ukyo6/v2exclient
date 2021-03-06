package com.ukyoo.v2client.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ukyoo.v2client.App
import androidx.room.migration.Migration
import com.ukyoo.v2client.data.dao.NodeModelDao
import com.ukyoo.v2client.data.dao.ProfilerDao
import com.ukyoo.v2client.data.entity.NodeModel
import com.ukyoo.v2client.data.entity.ProfileModel


/**
 * 创建数据库和Dao
 */
@Database(entities = [NodeModel::class, ProfileModel::class], version = 3)
abstract class AppDataBase : RoomDatabase() {
    abstract fun nodeModelDao(): NodeModelDao
    abstract fun profileModelDao(): ProfilerDao


    companion object {
        private val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `user_profile`(`uid` INTEGER NOT NULL, `username` TEXT, `avatar` TEXT,`nodes` INTEGER NOT NULL, `topics` INTEGER NOT NULL, `followings` INTEGER NOT NULL, `notifications` INTEGER NOT NULL,PRIMARY KEY(`uid`))"
                )
            }
        }

        @Volatile private var instance: AppDataBase? = null

        fun getDatabase(): AppDataBase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase().also { instance = it }
            }
        }

        private fun buildDatabase(): AppDataBase {
            return Room.databaseBuilder(
                App.instance(),
                AppDataBase::class.java, "database-v2ex"
            )
                .enableMultiInstanceInvalidation()
                .addMigrations(MIGRATION_2_3)
                .build()
        }
    }
}