package com.ukyoo.v2client.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ukyoo.v2client.viewmodels.*
import com.ukyoo.v2client.base.viewmodel.APPViewModelFactory
import com.ukyoo.v2client.base.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * 页面描述：ViewModelModule
 *
 * Created by hewei
 */
@Module
abstract class ViewModelModule{

//    @Binds
//    @IntoMap
//    @ActivityScope
//    @ViewModelKey(ArticleDetailViewModel::class)
//    abstract fun bindArticleDetailViewModel(viewModel: ArticleDetailViewModel): ViewModel
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(CodeDetailViewModel::class)
//    abstract fun bindCodeDetailViewModel(viewModel: CodeDetailViewModel):ViewModel
//
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun provideMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TopicsViewModel::class)
    abstract fun provideTopicsViewModel(viewModel: TopicsViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NodesViewModel::class)
    abstract fun provideNodesViewModel(viewModel: NodesViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    abstract fun provideDetailViewModel(viewmodel:DetailViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun provideLoginViewModel(viewModel:LoginViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserInfoViewModel::class)
    abstract fun provideUserInfoViewModel(viewModel:UserInfoViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PersonalViewModel::class)
    abstract fun providePersonViewModel(viewModel:PersonalViewModel):ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: APPViewModelFactory): ViewModelProvider.Factory
}