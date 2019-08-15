package com.ukyoo.v2client.ui.node

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.uber.autodispose.autoDisposable
import com.ukyoo.v2client.base.viewmodel.AutoDisposeViewModel
import com.ukyoo.v2client.data.Resources
import com.ukyoo.v2client.data.entity.NodeModel
import com.ukyoo.v2client.repository.NodesRepository
import com.ukyoo.v2client.util.ErrorHanding
import com.ukyoo.v2client.util.async
import javax.inject.Inject

class NodesViewModel @Inject constructor(val repository: NodesRepository) : AutoDisposeViewModel() {

    //节点名称
    private val _queryName = MutableLiveData<String>()

    fun setQueryName(queryName: String) {
        if (_queryName.value == queryName) {
            return
        }
        _queryName.value = queryName
    }

    /**
     *  根据节点名称查询数据
     */
    val nodesLiveData: LiveData<Resources<List<NodeModel>>> = Transformations.switchMap(_queryName) { searchName ->
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


    private fun getAll(): LiveData<Resources<List<NodeModel>>> {
        val result = MutableLiveData<Resources<List<NodeModel>>>()

        repository.getAllNodes()
            .async()
            .doOnSubscribe { result.value = Resources.loading() }
            .autoDisposable(this@NodesViewModel)
            .subscribe(
                { result.value = Resources.success(it) },
                { msg -> result.value = Resources.error(ErrorHanding.handleError(msg)) }
            )
        return result
    }

    private fun search(searchName: String): LiveData<Resources<List<NodeModel>>> {
        val result = MutableLiveData<Resources<List<NodeModel>>>()

        repository.queryByName(searchName)
            .async()
            .doOnSubscribe { result.value = Resources.loading() }
            .autoDisposable(this@NodesViewModel)
            .subscribe(
                { result.value = Resources.success(it) },
                { msg -> result.value = Resources.error(ErrorHanding.handleError(msg)) }
            )
        return result
    }


}