package com.ukyoo.v2client.viewmodel

import androidx.lifecycle.MutableLiveData
import com.uber.autodispose.autoDisposable
import com.ukyoo.v2client.base.viewmodel.AutoDisposeViewModel
import com.ukyoo.v2client.data.Resources
import com.ukyoo.v2client.data.api.NetManager
import com.ukyoo.v2client.entity.ProfileModel
import com.ukyoo.v2client.repository.ProfilerRepository
import com.ukyoo.v2client.util.ErrorHanding
import com.ukyoo.v2client.util.async
import javax.inject.Inject

class ProfilerViewModel @Inject constructor(private val repository: ProfilerRepository) : AutoDisposeViewModel() {


    val userProfilerLiveData = MutableLiveData<Resources<ProfileModel>>()


    fun refreshProfiler() {
        repository.getUserProfiler()
            .async()
            .autoDisposable(this)
            .subscribe(
                { userProfilerLiveData.value = Resources.success(it) },
                { userProfilerLiveData.value = Resources.error(ErrorHanding.handleError(it)) }
            )
    }


    fun setUserProfiler(model: ProfileModel) {
        userProfilerLiveData.value = Resources.success(model)
    }

    /**
     * 退出登录
     */
    fun signOut() {
        NetManager.clearCookie()
        userProfilerLiveData.value = null
    }
}