package com.ukyoo.v2client.di.component

import com.ukyoo.v2client.di.module.FragmentModule
import com.ukyoo.v2client.di.scope.FragmentScope
import com.ukyoo.v2client.ui.main.*
import dagger.Subcomponent

/**
 * 页面描述：FragmentComponent
 *
 * Created by ditclear on 2017/9/29.
 */
@FragmentScope
@Subcomponent(modules = [FragmentModule::class])
interface FragmentComponent {

    fun inject(fragment: MainFragment)
    fun inject(fragment: HomeFragment)
    fun inject(fragment: NodesFragment)
    fun inject(fragment: PersonalFragment)
    fun inject(fragment: TopicsFragment)


    @Subcomponent.Builder
    interface Builder {
        fun fragmentModule(module: FragmentModule): Builder

        fun build(): FragmentComponent
    }
}