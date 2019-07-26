package com.ukyoo.v2client.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ukyoo.v2client.base.viewmodel.AutoDisposeViewModel
import com.ukyoo.v2client.entity.UserInfoModel
import com.ukyoo.v2client.repository.UserInfoRepository
import com.ukyoo.v2client.util.AbsentLiveData
import javax.inject.Inject

/**
 * UserInfo ViewModel
 */
class UserInfoViewModel @Inject constructor(
    private var repository: UserInfoRepository
) : AutoDisposeViewModel() {


    private val _userName: MutableLiveData<String> = MutableLiveData()

    fun setUserName(userName: String) {
        _userName.value = userName
    }

    //用户信息
    var userInfo: LiveData<UserInfoModel> = Transformations.switchMap(_userName) { userName ->
        if(userName == null){
             AbsentLiveData.create()
        } else {
            LiveDataReactiveStreams.fromPublisher(repository.getUserInfo(userName))
        }
    }
}