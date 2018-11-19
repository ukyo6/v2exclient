package com.ukyoo.v2client.di.component

import com.ukyoo.v2client.di.module.ActivityModule
import com.ukyoo.v2client.di.scope.ActivityScope
import dagger.Subcomponent

/**
 * 页面描述：ActivityComponent
 *
 * Created by ditclear on 2017/9/29.
 */
@ActivityScope
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {


    fun supplyFragmentComponentBuilder(): FragmentComponent.Builder

}