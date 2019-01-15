package com.ukyoo.v2client.viewmodels

import android.text.TextUtils
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger
import com.ukyoo.v2client.api.JsonService
import com.ukyoo.v2client.db.NodeModelDao
import com.ukyoo.v2client.entity.NodeModel
import com.ukyoo.v2client.util.ErrorHanding
import com.ukyoo.v2client.util.ToastUtil
import com.ukyoo.v2client.util.async
import com.ukyoo.v2client.base.viewmodel.PagedViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NodesViewModel @Inject constructor(var apiService: JsonService) : PagedViewModel() {

    @Inject
    lateinit var nodeModelDao: NodeModelDao

    var nodesList = ObservableArrayList<NodeModel>()

    //网络请求数据 保存到db
    fun loadData() {
        apiService.getAllNodes()
            .map {
                nodeModelDao.apply {
                    deleteAll()
                    insertAll(it)
                }
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
        //没有条件就加载全部的
        if (TextUtils.isEmpty(name)) {
            nodeModelDao.getAll()
                .async()
                .subscribe({
                    nodesList.clear()
                    nodesList.addAll(it)
                }, {
                    ToastUtil.shortShow("查询失败")
                })
            return
        }

        //根据条件查询
        nodeModelDao
            .queryNodesByName("%$name%")
            .async()
            .throttleFirst(100,TimeUnit.MILLISECONDS) //限制一下搜索的频率
            .subscribe({
                nodesList.clear()
                nodesList.addAll(it)
            }, {
                ToastUtil.shortShow("查询失败")
            })
    }
}