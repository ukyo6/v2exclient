package com.ukyoo.v2client.di.component

import com.ukyoo.v2client.di.module.ActivityModule
import com.ukyoo.v2client.di.scope.ActivityScope
import com.ukyoo.v2client.ui.detail.DetailActivity
import com.ukyoo.v2client.ui.login.LoginActivity
import com.ukyoo.v2client.ui.main.MainActivity
import com.ukyoo.v2client.ui.userinfo.UserInfoActivity
import dagger.Subcomponent

/**
 * 页面描述：ActivityComponent
 *
 * Created by ditclear on 2017/9/29.
 */
@ActivityScope
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {

    fun inject(activity: MainActivity)

    fun inject(activity: DetailActivity)

    fun inject(activity: LoginActivity)

    fun inject(activity: UserInfoActivity)

    fun supplyFragmentComponentBuilder(): FragmentComponent.Builder

}