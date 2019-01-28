package com.ukyoo.v2client.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ukyoo.v2client.data.entity.ProfileModel
import io.reactivex.Flowable

@Dao
interface ProfileModelDao {

    @Query("SELECT * from user_profile LIMIT 1")
    fun getUserProfile(): Flowable<ProfileModel>

    @Insert
    fun saveUserProfile(user: ProfileModel)

}