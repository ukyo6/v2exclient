package com.ukyoo.v2client.di.module

import androidx.appcompat.app.AppCompatActivity
import com.ukyoo.v2client.di.component.FragmentComponent
import dagger.Module
import dagger.Provides

/**
 * 页面描述：ActivityModule
 *
 * Created by ditclear on 2017/9/26.
 */
@Module(subcomponents = [(FragmentComponent::class)])
class ActivityModule(private val activity: AppCompatActivity){

    @Provides
    fun provideActivity():AppCompatActivity=activity


}