package com.ukyoo.v2client.viewmodels

import androidx.databinding.ObservableArrayList
import com.ukyoo.v2client.R.id.name
import com.ukyoo.v2client.base.viewmodel.PagedViewModel
import com.ukyoo.v2client.data.api.HtmlService
import com.ukyoo.v2client.data.api.JsonService
import com.ukyoo.v2client.entity.TopicModel
import com.ukyoo.v2client.repository.TopicListRepository
import com.ukyoo.v2client.util.ErrorHanding
import com.ukyoo.v2client.util.ToastUtil
import com.ukyoo.v2client.util.async
import java.util.Collections.addAll
import javax.inject.Inject
import javax.inject.Named

/**
 * @desc 单个主题列表页ViewModel
 * @author hewei
 * @time
 */
class TopicListViewModel @Inject constructor(@Named("non_cached") private var apiService: HtmlService, private var jsonService: JsonService) :
    PagedViewModel() {

    internal lateinit var name: String //topicName
    internal lateinit var tab: String  //tabId

    var list = ObservableArrayList<TopicModel>()

    /**
     * loadData by tabId
     */
    fun loadDataByTab(isRefresh: Boolean) {
        apiService.queryTopicsByTab(tab)
            .map { response ->
                return@map TopicListRepository.getInstance().parse(response) //子线程解析html ->json
            }
            .async()
            .doOnSubscribe {
                startLoad()
            }.doFinally {
                stopLoad()
                empty.set(list.isEmpty())
            }
            .subscribe({ topics ->
                if (isRefresh) list.clear()
                list.addAll(topics)
            }, {
                ToastUtil.shortShow(ErrorHanding.handleError(it))
            })
    }


    /**
     * loadData by topicName
     */
    fun loadDataByName(isRefresh: Boolean) {
        jsonService.queryTopicsByName(name)
            .map { response ->
                if (isRefresh) {
                    list.clear()
                }
                return@map response.apply {
                    list.addAll(this)
                }
            }
            .async()
            .doOnSubscribe {
                startLoad()
            }.doAfterTerminate {
                stopLoad()
                empty.set(list.isEmpty())
            }.subscribe({}, {
                ToastUtil.shortShow(ErrorHanding.handleError(it))
            })
    }
}

