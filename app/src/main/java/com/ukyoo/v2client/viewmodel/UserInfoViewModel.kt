package com.ukyoo.v2client.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.uber.autodispose.autoDisposable
import com.ukyoo.v2client.base.viewmodel.AutoDisposeViewModel
import com.ukyoo.v2client.data.Resources
import com.ukyoo.v2client.entity.UserInfoModel
import com.ukyoo.v2client.repository.UserInfoRepository
import com.ukyoo.v2client.util.AbsentLiveData
import com.ukyoo.v2client.util.ErrorHanding
import com.ukyoo.v2client.util.async
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * UserInfo ViewModel
 */
class UserInfoViewModel @Inject constructor(
    private val repository: UserInfoRepository
) : AutoDisposeViewModel() {


    private val _userName: MutableLiveData<String> = MutableLiveData()

    fun setUserName(userName: String) {
        _userName.value = userName
    }

    //用户信息
    var userInfo: LiveData<Resources<UserInfoModel>> = Transformations.switchMap(_userName) { userName ->
        if (userName == null) {
            AbsentLiveData.create()
        } else {
            getUserInfo(userName)
        }
    }

    private fun getUserInfo(userName: String): LiveData<Resources<UserInfoModel>> {

        val result = MutableLiveData<Resources<UserInfoModel>>()

        repository.getUserInfo(userName)
            .async()
            .startWith {
                result.value = Resources.loading()
            }
            .doOnSubscribe { result.value = Resources.loading() }
            .autoDisposable(this)
            .subscribe(
                { result.value = Resources.success(it) },
                { result.value = Resources.error(ErrorHanding.handleError(it)) }
            )
        return result
    }
}