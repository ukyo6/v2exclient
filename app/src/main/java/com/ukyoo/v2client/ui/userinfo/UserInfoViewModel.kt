package com.ukyoo.v2client.ui.userinfo

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ukyoo.v2client.base.viewmodel.PagedViewModel
import com.ukyoo.v2client.data.api.HtmlService
import com.ukyoo.v2client.data.api.JsonService
import com.ukyoo.v2client.entity.MemberModel
import com.ukyoo.v2client.entity.ReplyItem
import javax.inject.Inject
import javax.inject.Named

/**
 * UserInfo ViewModel
 */
class UserInfoViewModel @Inject constructor(
    private var jsonService: JsonService,
    @Named("non_cached") private var htmlService: HtmlService
) : PagedViewModel() {

    var memberModel = ObservableField<ReplyItem.MemberInfo>()  //用户信息

    var registerTimeAndNum = ObservableField<String>()  //注册时间


    private val _userName: MutableLiveData<String> = MutableLiveData()

    fun setUserName(userName: String) {
        _userName.value = userName
    }


//    var userInfo: MutableLiveData<MemberModel> = Transformations.switchMap(_userName) { userName ->
//
//    }


}