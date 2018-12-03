package com.ukyoo.v2client.ui.viewmodels

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.ViewModel
import com.ukyoo.v2client.api.JsonService
import com.ukyoo.v2client.entity.TopicListModel
import com.ukyoo.v2client.entity.TopicModel
import com.ukyoo.v2client.util.async
import com.ukyoo.v2client.viewmodel.PagedViewModel
import io.reactivex.Single
import java.util.ArrayList
import javax.inject.Inject

class NodesViewMode @Inject constructor(var apiService: JsonService): PagedViewModel(){

    var list = ObservableArrayList<Any>()


    //request remote data
    fun loadData(isRefresh: Boolean): Single<ArrayList<TopicModel>> {

        return apiService.getAllNodes()
            .async()
            .map { response ->
               return@map null

            }.doOnSubscribe {
                startLoad()
            }.doAfterTerminate {
                stopLoad()
                empty.set(list.isEmpty())
            }

    }
}