package com.ukyoo.v2client.ui.viewmodels

import androidx.databinding.ObservableArrayList
import com.ukyoo.v2client.api.ApiService
import com.ukyoo.v2client.entity.TopicListModel
import com.ukyoo.v2client.entity.TopicModel
import com.ukyoo.v2client.entity.V2EXDateModel
import com.ukyoo.v2client.entity.V2EXModel
import com.ukyoo.v2client.util.async
import com.ukyoo.v2client.viewmodel.PagedViewModel
import io.reactivex.Single
import java.util.ArrayList
import javax.inject.Inject

class TopicsViewModel @Inject constructor(private var apiService: ApiService) : PagedViewModel() {

    //the id of each topic
    internal lateinit var topicId: String

    public var list = ObservableArrayList<TopicModel>()

    //request remote data
    fun loadData(isRefresh: Boolean): Single<ArrayList<TopicModel>> {

        return apiService.queryTopics(topicId)
            .async()
            .map { response ->
                if (isRefresh) {
                    list.clear()
                }
                return@map TopicListModel().parse(response)
            }.doOnSubscribe {
                startLoad()
            }.doAfterTerminate {
                stopLoad()
                empty.set(list.isEmpty())
            }
    }


}

