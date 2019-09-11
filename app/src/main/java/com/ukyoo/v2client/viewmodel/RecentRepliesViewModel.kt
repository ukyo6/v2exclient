package com.ukyoo.v2client.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.uber.autodispose.autoDisposable
import com.ukyoo.v2client.base.viewmodel.AutoDisposeViewModel
import com.ukyoo.v2client.data.Resources
import com.ukyoo.v2client.data.entity.UserReplyModel
import com.ukyoo.v2client.repository.UserInfoRepository
import com.ukyoo.v2client.util.AbsentLiveData
import com.ukyoo.v2client.util.ErrorHanding
import com.ukyoo.v2client.util.async
import javax.inject.Inject

/**
 * 用户信息
 * 最近的回复列表 ViewModel
 */
class RecentRepliesViewModel @Inject constructor(val repository: UserInfoRepository) : AutoDisposeViewModel() {

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
    var userReplies: LiveData<Resources<ArrayList<UserReplyModel>>> = Transformations.switchMap(param) { value ->
        value.ifExists { userName, page ->
            getUserReplies(userName, page)
        }
    }

    /**
     * 获取用户回复
     */
    private fun getUserReplies(userName: String, page: Int): LiveData<Resources<ArrayList<UserReplyModel>>> {
        val result = MutableLiveData<Resources<ArrayList<UserReplyModel>>>()

        repository.getUserReplies(userName, page)
            .async()
            .doOnSubscribe { result.value = Resources.loading() }
            .autoDisposable(this)
            .subscribe(
                { data ->
                    result.value = Resources.success(data)
                },
                { error ->
                    result.value = Resources.error(ErrorHanding.handleError(error))
                }
            )
        return result
    }

    /**
     *  重试
     */
    fun retry() {
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