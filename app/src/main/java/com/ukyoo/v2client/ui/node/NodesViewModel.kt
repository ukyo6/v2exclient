package com.ukyoo.v2client.ui.node

import android.text.TextUtils
import androidx.databinding.ObservableArrayList
import com.ukyoo.v2client.data.api.JsonService
import com.ukyoo.v2client.data.db.NodeModelDao
import com.ukyoo.v2client.data.entity.NodeModel
import com.ukyoo.v2client.util.ErrorHanding
import com.ukyoo.v2client.util.ToastUtil
import com.ukyoo.v2client.util.async
import com.ukyoo.v2client.base.viewmodel.PagedViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NodesViewModel @Inject constructor(private var apiService: JsonService) : PagedViewModel() {

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
            .doOnSubscribe {
                startLoad()
            }.doAfterTerminate {
                stopLoad()
                empty.set(nodesList.isEmpty())
            }
            .async()
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