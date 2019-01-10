package com.ukyoo.v2client.viewmodels

import androidx.databinding.ObservableField
import com.ukyoo.v2client.api.JsonService
import com.ukyoo.v2client.base.viewmodel.BaseViewModel
import com.ukyoo.v2client.base.viewmodel.PagedViewModel
import com.ukyoo.v2client.entity.MemberModel
import com.ukyoo.v2client.util.ErrorHanding
import com.ukyoo.v2client.util.ToastUtil
import com.ukyoo.v2client.util.async
import javax.inject.Inject

class UserInfoViewModel@Inject constructor(private var jsonService: JsonService) : BaseViewModel() {

    var memberModel = ObservableField<MemberModel>()

    fun getUserInfo(username:String, isRefresh:Boolean){
        jsonService.getUserInfo(username)
            .async()
            .doOnSubscribe {
//                startLoad()
            }.doAfterTerminate {
//                stopLoad()
            }.subscribe({
                memberModel.set(it[0])
            },{
                ToastUtil.shortShow(ErrorHanding.handleError(it))
            })
    }
}