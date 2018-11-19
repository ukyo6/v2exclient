package com.ukyoo.v2client.di.component

import com.ukyoo.v2client.di.module.FragmentModule
import com.ukyoo.v2client.di.scope.FragmentScope
import dagger.Subcomponent

/**
 * 页面描述：FragmentComponent
 *
 * Created by ditclear on 2017/9/29.
 */
@FragmentScope
@Subcomponent(modules = arrayOf(FragmentModule::class))
interface FragmentComponent {

//    fun inject(fragment: ArticleListFragment)
//    fun inject(fragment: CodeListFragment)
//
//    fun inject(fragment: CollectionListFragment)
//
//    fun inject(fragment: MyCollectFragment)
//
//    fun inject(fragment: HomeFragment)
//
//    fun inject(fragment: RecentFragment)
//
//
//    fun inject(fragment: SearchResultFragment)
//
//    fun inject(fragment: RecentSearchFragment)
//
//    fun inject(fragment: MyArticleFragment)

    @Subcomponent.Builder
    interface Builder {
        fun fragmentModule(module: FragmentModule): Builder

        fun build(): FragmentComponent
    }
}