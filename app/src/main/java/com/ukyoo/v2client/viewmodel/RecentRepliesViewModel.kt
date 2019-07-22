package com.ukyoo.v2client.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ukyoo.v2client.base.viewmodel.BaseViewModel
import com.ukyoo.v2client.data.entity.ReplyListModel
import com.ukyoo.v2client.repository.UserInfoRepository
import com.ukyoo.v2client.util.AbsentLiveData
import com.ukyoo.v2client.util.ErrorHanding
import com.ukyoo.v2client.util.ToastUtil
import java.util.Collections.addAll
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
    var userReplies : LiveData<ArrayList<ReplyListModel.ReplyItemModel>> = Transformations.switchMap(param) { value ->

        value.ifExists { userName, page ->
            LiveDataReactiveStreams.fromPublisher {
                repository.getUserReplies(userName,page)
            }
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