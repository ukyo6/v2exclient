package com.ukyoo.v2client.viewmodel

import androidx.lifecycle.MutableLiveData
import com.uber.autodispose.autoDisposable
import com.ukyoo.v2client.App
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.viewmodel.AutoDisposeViewModel
import com.ukyoo.v2client.data.Resource
import com.ukyoo.v2client.data.api.NetManager
import com.ukyoo.v2client.entity.ProfileModel
import com.ukyoo.v2client.repository.ProfilerRepository
import com.ukyoo.v2client.util.ErrorHanding
import com.ukyoo.v2client.util.SPUtils
import com.ukyoo.v2client.util.async
import com.ukyoo.v2client.util.bindLifeCycle
import javax.inject.Inject

class ProfilerViewModel @Inject constructor(private val repository: ProfilerRepository) : AutoDisposeViewModel() {


//    val userProfilerLiveData = repository.getUserProfiler()
    val userProfilerLiveData = MutableLiveData<Resource<ProfileModel>>()


    fun refreshProfiler(){
        repository.getUserProfiler()
            .async()
            .autoDisposable(this)
            .subscribe({
                if(it.username.isEmpty()) {
                    userProfilerLiveData.value = Resource.empty()
                } else {
                    userProfilerLiveData.value = Resource.success(it)
                }
            }, {
                userProfilerLiveData.value = Resource.error(ErrorHanding.handleError(it))
            })
    }



    fun setUserProfiler(model: ProfileModel) {
        userProfilerLiveData.value = Resource.success(model)
    }

    /**
     * 退出登录
     */
    fun signOut() {
        NetManager.clearCookie()
        userProfilerLiveData.setValue(null)
    }
}