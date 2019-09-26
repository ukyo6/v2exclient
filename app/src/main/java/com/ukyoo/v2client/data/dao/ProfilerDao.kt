package com.ukyoo.v2client.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ukyoo.v2client.data.entity.ProfileModel
import io.reactivex.Flowable

@Dao
interface ProfilerDao {

    @Query("SELECT * from user_profile")
    fun getUserProfile(): Flowable<ProfileModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveUserProfile(user: ProfileModel)

}