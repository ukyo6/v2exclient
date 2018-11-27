package com.ukyoo.v2client.ui.viewmodels

import androidx.databinding.ObservableArrayList
import com.ukyoo.v2client.api.ApiService
import com.ukyoo.v2client.entity.TopicEntity
import com.ukyoo.v2client.util.async
import com.ukyoo.v2client.viewmodel.PagedViewModel
import io.reactivex.Single
import javax.inject.Inject

class TopicsViewModel @Inject constructor(private var apiService: ApiService) : PagedViewModel() {

    //the id of each topic
    internal lateinit var topicId: String

    private var list = ObservableArrayList<TopicEntity>()

    fun loadData(isRefresh: Boolean): Single<Boolean> =

        apiService.queryTopics(topicId)
            .async()
            .map { it ->
                if (isRefresh) {
                    list.clear()
                }
                return@map it.let { list.addAll(it) }
            }.doOnSubscribe {
                startLoad()
            }.doAfterTerminate {
                stopLoad()
                empty.set(list.isEmpty())
            }

}

