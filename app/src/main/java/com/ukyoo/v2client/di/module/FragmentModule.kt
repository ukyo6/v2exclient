package com.ukyoo.v2client.di.module

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import dagger.Module
import dagger.Provides
import javax.inject.Named


/**
 * 页面描述：FragmentModule
 *
 * Created by ditclear on 2017/9/26.
 */
@Module
class FragmentModule(private val fragment: Fragment) {

    @Provides
    fun provideFragment() = fragment

    @Provides
    fun provideContext(): Context = fragment.context ?: throw Exception("fragment context is null")

    @Provides
    fun provideFragmentManager(): FragmentManager = fragment.childFragmentManager

//    @Provides
//    @Named(Constants.Qualifier_HOME)
//    fun provideHomePagerAdapter(): FragmentStatePagerAdapter {
//
//        return object : BaseViewPagerAdapter(fragment.childFragmentManager, arrayOf("Recent", "ANDROID", "程序设计", "前端开发", "IOS", "数据库", "开发日志", "应用推荐", "每日一站")) {
//            override fun getItem(pos: Int): Fragment? {
//                if (list[pos] == null) {
//                    when (pos) {
//                        0 -> list[pos] = RecentFragment.newInstance()
//                        1 -> list[pos] = ArticleListFragment.newInstance(ArticleType.ANDROID)
//                        2 -> list[pos] = ArticleListFragment.newInstance(ArticleType.PROGRAME)
//                        3 -> list[pos] = ArticleListFragment.newInstance(ArticleType.FRONT_END)
//                        4 -> list[pos] = ArticleListFragment.newInstance(ArticleType.IOS)
//                        5 -> list[pos] = ArticleListFragment.newInstance(ArticleType.DB)
//                        6 -> list[pos] = ArticleListFragment.newInstance(ArticleType.DEVLOG)
//                        7 -> list[pos] = ArticleListFragment.newInstance(ArticleType.RECOMMAND)
//                        8 -> list[pos] = ArticleListFragment.newInstance(ArticleType.DAILY)
//                    }
//                }
//                return list[pos]
//            }
//        }
//    }
//
//    @Provides
//    @Named(Constants.Qualifier_COLLECT)
//    fun provideCollectPagerAdapter(): FragmentStatePagerAdapter {
//        return object : BaseViewPagerAdapter(fragment.childFragmentManager, arrayOf("文章", "代码")) {
//            override fun getItem(pos: Int): Fragment? {
//                if (list[pos] == null) {
//                    when (pos) {
//                        0 -> list[pos] = CollectionListFragment.newInstance(1)
//                        1 -> list[pos] = CollectionListFragment.newInstance(-19)
//                    }
//                }
//                return list[pos]
//            }
//        }
//    }
//
//    @Provides
//    @Named(Constants.Qualifier_SEARCH)
//    fun provideSearchPagerAdapter(): FragmentStatePagerAdapter {
//        return object : BaseViewPagerAdapter(fragment.childFragmentManager, arrayOf("文章", "代码")) {
//            val keyWord= (fragment as? SearchResultFragment)?.keyWord ?: ""
//            override fun getItem(pos: Int): Fragment? {
//                if (list[pos] == null) {
//                    when (pos) {
//                        0 -> list[pos] = ArticleListFragment.newInstance(keyWord)
//                        1 -> list[pos] = CodeListFragment.newInstance(keyWord)
//                    }
//                }
//                return list[pos]
//            }
//
//        }
//    }


}