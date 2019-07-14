package com.ukyoo.v2client.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ukyoo.v2client.viewmodel.*
import com.ukyoo.v2client.base.viewmodel.APPViewModelFactory
import com.ukyoo.v2client.base.viewmodel.ViewModelKey
import com.ukyoo.v2client.ui.detail.DetailViewModel
import com.ukyoo.v2client.ui.login.LoginViewModel
import com.ukyoo.v2client.ui.node.NodesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * 页面描述：ViewModelModule
 *
 * Created by hewei
 */
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(TopicListViewModel::class)
    abstract fun provideTopicsViewModel(viewModel: TopicListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NodesViewModel::class)
    abstract fun provideNodesViewModel(viewModel: NodesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    abstract fun provideDetailViewModel(viewmodel: DetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun provideLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserInfoViewModel::class)
    abstract fun provideUserInfoViewModel(viewModel: UserInfoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PersonalViewModel::class)
    abstract fun providePersonViewModel(viewModel: PersonalViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RecentRepliesViewModel::class)
    abstract fun provideRecentRepliesViewModel(viewModel: RecentRepliesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RecentTopicsViewModel::class)
    abstract fun provideRcecentTopicsViewModel(viewModel: RecentTopicsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: APPViewModelFactory): ViewModelProvider.Factory
}