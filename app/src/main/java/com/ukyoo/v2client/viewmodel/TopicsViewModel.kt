package com.ukyoo.v2client.viewmodel

import androidx.lifecycle.LifecycleOwner
import com.orhanobut.logger.Logger
import com.ukyoo.v2client.base.viewmodel.PagedViewModel
import com.ukyoo.v2client.entity.TopicModel
import com.ukyoo.v2client.repository.TopicsRepository
import com.ukyoo.v2client.util.ContentUtils
import org.jsoup.Jsoup
import java.util.ArrayList

/**
 * 列表主题页ViewModel
 */
class TopicsViewModel : PagedViewModel() {


    lateinit var name: String //topicName
    lateinit var tab: String  //tabId

    var isRefresh: Boolean = false



    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
    }

    fun getDataByName() {
        TopicsRepository.getInstance().loadDataByName(isRefresh, name)
    }

    fun getDataByTab(){
        TopicsRepository.getInstance().loadDataByTab(isRefresh, tab)
    }




}

