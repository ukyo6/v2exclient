package com.ukyoo.v2client.viewmodels

import android.content.Intent
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.ObservableField
import com.ukyoo.v2client.api.HtmlService
import com.ukyoo.v2client.base.viewmodel.BaseViewModel
import com.ukyoo.v2client.entity.ProfileModel
import com.ukyoo.v2client.navigator.LoginNavigator
import com.ukyoo.v2client.navigator.PersonalNavigator
import com.ukyoo.v2client.ui.login.LoginActivity
import com.ukyoo.v2client.util.ErrorHanding
import com.ukyoo.v2client.util.SPUtils
import com.ukyoo.v2client.util.ToastUtil
import com.ukyoo.v2client.util.async
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Named

class PersonalViewModel @Inject constructor(@Named("cached") private var htmlService: HtmlService) : BaseViewModel() {

    var profileModel = ObservableField<ProfileModel>()

    /**
     * 获取用户基本信息
     */
    fun getUserProfiler() {
        htmlService.getMyNodes()
            .async()
            .subscribe({
                profileModel.set(ProfileModel().apply {
                    parse(it)
                })
            }, {
                ToastUtil.shortShow(ErrorHanding.handleError(it))
            })
    }


    @Nullable
    private var navigator: WeakReference<PersonalNavigator>? = null

    fun setPersonalNavigator(navigator: PersonalNavigator) {
        this.navigator = WeakReference(navigator)
    }

    fun gotoLogin(){
        if (navigator != null && navigator?.get() != null) {
            navigator?.get()?.gotoLogin()
        }
    }
}