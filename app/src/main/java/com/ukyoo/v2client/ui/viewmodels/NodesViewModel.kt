package com.ukyoo.v2client.ui.viewmodels

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.ViewModel
import com.ukyoo.v2client.api.JsonService
import com.ukyoo.v2client.entity.NodeModel
import com.ukyoo.v2client.entity.TopicListModel
import com.ukyoo.v2client.entity.TopicModel
import com.ukyoo.v2client.util.async
import com.ukyoo.v2client.viewmodel.PagedViewModel
import io.reactivex.Single
import java.util.ArrayList
import javax.inject.Inject

class NodesViewModel @Inject constructor(var apiService: JsonService): PagedViewModel(){


    companion object {
        val aa = 1
    }

    var nodesList = ObservableArrayList<NodeModel>()

    //request remote data
    fun loadData(isRefresh: Boolean): Single<ArrayList<NodeModel>> {
        return apiService.getAllNodes()
            .async()
            .map {
                if(isRefresh){
                    nodesList.clear()
                }
                nodesList.addAll(it)
                return@map it
            }
            .doOnSubscribe {
                startLoad()
            }.doAfterTerminate {
                stopLoad()
                empty.set(nodesList.isEmpty())
            }
    }
}