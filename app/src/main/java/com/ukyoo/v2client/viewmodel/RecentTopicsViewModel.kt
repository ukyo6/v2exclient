package com.ukyoo.v2client.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.uber.autodispose.autoDisposable
import com.ukyoo.v2client.base.viewmodel.AutoDisposeViewModel
import com.ukyoo.v2client.data.Resource
import com.ukyoo.v2client.entity.TopicModel
import com.ukyoo.v2client.repository.UserInfoRepository
import com.ukyoo.v2client.util.AbsentLiveData
import io.reactivex.Flowable
import javax.inject.Inject

class RecentTopicsViewModel @Inject constructor(val repository: UserInfoRepository) : AutoDisposeViewModel() {

    //参数 用户名.页码
    private val param: MutableLiveData<RecentTopicsParam> = MutableLiveData()

    fun setUserNameAndPage(userName: String, page: Int) {
        val update = RecentTopicsParam(userName, page)
        if (param.value == update) {
            return
        }
        param.value = update
    }

    //用户的主题列表
    var userTopics: LiveData<Resource<ArrayList<TopicModel>>> = Transformations.switchMap(param) { value ->

        value.ifExists { userName, page ->
            repository.getUserTopics(userName, page)
        }
    }

    fun haha(){
        Flowable.just(1)
            .autoDisposable(this)
    }

    /**
     * 重试
     */
    fun retry(){
        val userName = param.value?.userName
        val page = param.value?.page
        if (userName != null && page != null) {
            param.value = RecentTopicsParam(userName, page)
        }
    }


    private data class RecentTopicsParam(val userName: String, val page: Int = 1) {
        fun <T> ifExists(f: (String, Int) -> LiveData<T>): LiveData<T> {
            return if (userName.isBlank() || page < 0) {
                AbsentLiveData.create()
            } else {
                f(userName, page)
            }
        }
    }
}