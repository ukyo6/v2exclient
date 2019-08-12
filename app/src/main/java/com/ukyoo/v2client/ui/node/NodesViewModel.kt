package com.ukyoo.v2client.ui.node

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.orhanobut.logger.Logger
import com.uber.autodispose.autoDisposable
import com.ukyoo.v2client.base.viewmodel.AutoDisposeViewModel
import com.ukyoo.v2client.data.Resource
import com.ukyoo.v2client.data.Status
import com.ukyoo.v2client.data.db.NodeModelDao
import com.ukyoo.v2client.data.entity.NodeModel
import com.ukyoo.v2client.repository.NodesRepository
import com.ukyoo.v2client.util.ErrorHanding
import com.ukyoo.v2client.util.ToastUtil
import com.ukyoo.v2client.util.async
import com.ukyoo.v2client.viewmodel.TopicsViewModel
import javax.inject.Inject

class NodesViewModel @Inject constructor(val repository: NodesRepository) : AutoDisposeViewModel() {

    private fun getAll(): MutableLiveData<Resource<List<NodeModel>>> {
        val result = MutableLiveData<Resource<List<NodeModel>>>()
        repository.getAllNodes()
            .async()
            .doOnSubscribe {
                result.value = Resource.loading()
            }
            .autoDisposable(this@NodesViewModel)
            .subscribe({
                result.value = Resource.success(it)
            }, { msg ->
                result.value = Resource.error(ErrorHanding.handleError(msg))
            })
        return result
    }

    private fun search(searchName: String): MutableLiveData<Resource<List<NodeModel>>> {
        val result = MutableLiveData<Resource<List<NodeModel>>>()
        repository.queryByName(searchName)
            .async()
            .doOnSubscribe {
                result.value = Resource.loading()
            }
            .autoDisposable(this@NodesViewModel)
            .subscribe({
                result.value = Resource.success(it)
            }, { msg ->
                result.value = Resource.error(ErrorHanding.handleError(msg))
            })
        return result
    }


    private val _queryName = MutableLiveData<String>()
    fun setQueryName(queryName: String) {
        if (_queryName.value == queryName) {
            return
        }
        _queryName.value = queryName
    }

    //查询的数据
    val nodesLiveData: LiveData<Resource<List<NodeModel>>> = Transformations.switchMap(_queryName) { searchName ->
        if (searchName.isNullOrBlank()) {
            getAll()
        } else {
            search(searchName)
        }
    }

    /**
     * 重试
     */
    fun retry() {
        _queryName.value?.let {
            _queryName.value = it
        }
    }
}