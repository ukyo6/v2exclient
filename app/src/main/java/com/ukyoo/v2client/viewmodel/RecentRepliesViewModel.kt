package com.ukyoo.v2client.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ukyoo.v2client.base.viewmodel.BaseViewModel
import com.ukyoo.v2client.data.Resource
import com.ukyoo.v2client.entity.UserReplyModel
import com.ukyoo.v2client.repository.UserInfoRepository
import com.ukyoo.v2client.util.AbsentLiveData
import javax.inject.Inject

/**
 * 用户信息
 * 最近的回复列表 ViewModel
 */
class RecentRepliesViewModel @Inject constructor(val repository: UserInfoRepository) : BaseViewModel() {

    //参数 用户名.页码
    private val param: MutableLiveData<RecentRepliesParam> = MutableLiveData()

    fun setUserNameAndPage(userName: String, page: Int) {
        val update = RecentRepliesParam(userName, page)
        if (param.value == update) {
            return
        }
        param.value = update
    }

    //用户的回复列表
    var userReplies: LiveData<Resource<ArrayList<UserReplyModel>>> = Transformations.switchMap(param) { value ->

        value.ifExists { userName, page ->
            repository.getUserReplies(userName, page)
        }
    }

    /**
     *  重试
     */
    fun retry(){
        val userName = param.value?.userName
        val page = param.value?.page
        if (userName != null && page != null) {
            param.value = RecentRepliesParam(userName, page)
        }
    }



    private data class RecentRepliesParam(val userName: String, val page: Int = 1) {
        fun <T> ifExists(f: (String, Int) -> LiveData<T>): LiveData<T> {
            return if (userName.isBlank() || page < 0) {
                AbsentLiveData.create()
            } else {
                f(userName, page)
            }
        }
    }
}