package com.ukyoo.v2client.viewmodels

import com.ukyoo.v2client.api.HtmlService
import com.ukyoo.v2client.base.viewmodel.BaseViewModel
import com.ukyoo.v2client.util.SPUtils
import com.ukyoo.v2client.util.SingleLiveEvent
import javax.inject.Inject
import javax.inject.Named

class PersonalViewModel @Inject constructor(@Named("cached") private var htmlService: HtmlService) : BaseViewModel() {


    //liveData
    var hasLogin = SingleLiveEvent<Boolean>()

    fun getUserProfiler(){
        hasLogin.value = SPUtils.getBoolean("isLogin",false)
    }

    fun aa(){

    }
}