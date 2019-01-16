package com.ukyoo.v2client.navigator

import com.ukyoo.v2client.entity.ProfileModel

interface LoginNavigator {
    fun loginSuccess(model: ProfileModel)
}