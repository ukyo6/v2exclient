package com.ukyoo.v2client.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ukyoo.v2client.App
import com.ukyoo.v2client.entity.NodeModel
import com.ukyoo.v2client.entity.ProfileModel
import androidx.room.migration.Migration


/**
 * 创建数据库和Dao
 */
@Database(entities = [NodeModel::class, ProfileModel::class], version = 3)
abstract class AppDataBase : RoomDatabase() {
    abstract fun nodeModelDao(): NodeModelDao

    abstract fun profileModelDao(): ProfileModelDao

    companion object {
        private val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `user_profile`(`uid` INTEGER NOT NULL, `username` TEXT, `avatar` TEXT,`nodes` INTEGER NOT NULL, `topics` INTEGER NOT NULL, `followings` INTEGER NOT NULL, `notifications` INTEGER NOT NULL,PRIMARY KEY(`uid`))"
                )
            }
        }


        @Synchronized
        fun getDataBase(): AppDataBase {
            return Room.databaseBuilder(
                App.instance(),
                AppDataBase::class.java, "database-v2ex"
            )
                .addMigrations(MIGRATION_2_3)
                .build()
        }
    }
}