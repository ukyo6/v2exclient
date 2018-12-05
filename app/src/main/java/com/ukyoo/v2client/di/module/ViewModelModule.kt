package com.ukyoo.v2client.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ukyoo.v2client.ui.viewmodels.MainViewModel
import com.ukyoo.v2client.ui.viewmodels.NodesViewModel
import com.ukyoo.v2client.ui.viewmodels.TopicsViewModel
import com.ukyoo.v2client.viewmodel.APPViewModelFactory
import com.ukyoo.v2client.viewmodel.ViewModelKey
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


//
//    @Binds
//    @IntoMap
//    @ViewModelKey(ArticleListViewModel::class)
//    abstract fun bindArticleListViewModel(viewModel: ArticleListViewModel):ViewModel
//
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(CodeListViewModel::class)
//    abstract fun bindCodeListViewModel(viewModel: CodeListViewModel):ViewModel
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(RecentSearchViewModel::class)
//    abstract fun bindRecentSearchViewModel(viewModel: RecentSearchViewModel):ViewModel
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(MyArticleViewModel::class)
//    abstract fun bindMyArticleViewModel(viewModel: MyArticleViewModel):ViewModel
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(MyCollectViewModel::class)
//    abstract fun bindMyCollectViewModel(viewModel: MyCollectViewModel):ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: APPViewModelFactory): ViewModelProvider.Factory

}