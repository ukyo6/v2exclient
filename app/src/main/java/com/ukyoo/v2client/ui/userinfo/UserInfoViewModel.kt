package com.ukyoo.v2client.ui.userinfo

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ukyoo.v2client.base.viewmodel.PagedViewModel
import com.ukyoo.v2client.data.api.HtmlService
import com.ukyoo.v2client.data.api.JsonService
import com.ukyoo.v2client.entity.MemberModel
import com.ukyoo.v2client.entity.ReplyItem
import com.ukyoo.v2client.repository.UserInfoRepository
import com.ukyoo.v2client.util.AbsentLiveData
import javax.inject.Inject
import javax.inject.Named

/**
 * UserInfo ViewModel
 */
class UserInfoViewModel @Inject constructor(
    private var repository: UserInfoRepository
) : PagedViewModel() {


    private val _userName: MutableLiveData<String> = MutableLiveData()

    fun setUserName(userName: String) {
        _userName.value = userName
    }

    //用户信息
    var userInfo: LiveData<MemberModel> = Transformations.switchMap(_userName) { userName ->
        if(userName == null){
             AbsentLiveData.create()
        } else {
            LiveDataReactiveStreams.fromPublisher(repository.getUserInfo(userName))
        }
    }
}