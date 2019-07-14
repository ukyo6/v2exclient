package com.ukyoo.v2client.viewmodel

import com.ukyoo.v2client.base.viewmodel.PagedViewModel
import com.ukyoo.v2client.data.repository.TopicsRepository

/**
 * 列表主题页ViewModel
 */
class TopicsViewModel : PagedViewModel() {


    var name: String? = null//topicName
    var tab: String? = null  //tabId

    var isRefresh: Boolean = false


    var list = fun() {
        if (name != null) {
            TopicsRepository.getInstance().loadDataByName(isRefresh, name!!)
        } else {
            tab?.let { TopicsRepository.getInstance().loadDataByTab(isRefresh, it) }
        }
    }


}

