package com.ukyoo.v2client.navigator

import com.ukyoo.v2client.data.entity.ProfileModel

interface LoginNavigator {
    fun loginSuccess(model: ProfileModel)
}