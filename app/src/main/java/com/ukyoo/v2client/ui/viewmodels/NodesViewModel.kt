package com.ukyoo.v2client.ui.viewmodels

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.ViewModel
import com.orhanobut.logger.Logger
import com.ukyoo.v2client.api.JsonService
import com.ukyoo.v2client.db.AppDataBase
import com.ukyoo.v2client.db.NodeModelDao
import com.ukyoo.v2client.entity.NodeModel
import com.ukyoo.v2client.entity.TopicListModel
import com.ukyoo.v2client.entity.TopicModel
import com.ukyoo.v2client.util.ErrorHanding
import com.ukyoo.v2client.util.ToastUtil
import com.ukyoo.v2client.util.async
import com.ukyoo.v2client.util.bindLifeCycle
import com.ukyoo.v2client.viewmodel.PagedViewModel
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.ArrayList
import javax.inject.Inject

class NodesViewModel @Inject constructor(var apiService: JsonService) : PagedViewModel() {

    @Inject
    lateinit var nodeModelDao: NodeModelDao

    var nodesList = ObservableArrayList<NodeModel>()

    //网络请求数据 保存到db
    fun loadData() {
        apiService.getAllNodes()
            .map {
                nodeModelDao.insertAll(it)
                return@map it
            }
            .async()
            .doOnSubscribe {
                startLoad()
            }.doAfterTerminate {
                stopLoad()
                empty.set(nodesList.isEmpty())
            }
            .subscribe({
                nodesList.clear()
                nodesList.addAll(it)
            }, {
                ToastUtil.shortShow(ErrorHanding.handleError(it))
            })
    }

    //从db查询节点
    fun queryByName(name: String) {
        nodeModelDao
            .queryNodesByName(name)
            .async()
            .subscribe({
                nodesList.clear()
                nodesList.addAll(it)
            }, {
                ToastUtil.shortShow("查询失败")
            })
    }
}