package com.ukyoo.v2client.repository

import com.ukyoo.v2client.data.AppDataBase
import com.ukyoo.v2client.data.api.JsonService
import com.ukyoo.v2client.data.dao.NodeModelDao
import com.ukyoo.v2client.data.entity.NodeModel
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NodesRepository @Inject constructor(
    private val jsonService: JsonService,
    private val appDataBase: AppDataBase
) {

    //先看db有没有 没有就去请求全部的
    fun getAllNodes(): Flowable<List<NodeModel>> {
        return appDataBase.nodeModelDao().getAll()
            .flatMap { dbData ->
                if (dbData.isEmpty()) {
                    return@flatMap jsonService.getAllNodes()
                        .map {
                            appDataBase.nodeModelDao().apply {
                                deleteAll()
                                insertAll(it)
                            }
                            return@map it
                        }
                } else {
                    return@flatMap Flowable.just(dbData)
                }
            }
    }

    //没有条件就加载全部的
//    if (TextUtils.isEmpty(name)) {
//        nodeModelDao.getAll()
//            .subscribe({
//                nodesList.clear()
//                nodesList.addAll(it)
//            }, {
//                ToastUtil.shortShow("查询失败")
//            })
//        return
//    }


    //从db查询节点
    fun queryByName(name: String): Flowable<List<NodeModel>> {
        //根据条件查询
        return appDataBase.nodeModelDao()
            .queryNodesByName("%$name%")
            .throttleFirst(100, TimeUnit.MILLISECONDS) //限制一下搜索的频率

    }

}