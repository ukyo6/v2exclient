package com.ukyoo.v2client.ui.node

import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseActivity
import com.ukyoo.v2client.databinding.ActivityNodeBinding
import com.ukyoo.v2client.entity.NodeModel
import com.ukyoo.v2client.ui.main.TopicsFragment

class NodeActivity:BaseActivity<ActivityNodeBinding>(){
    override fun loadData(isRefresh: Boolean) {

    }

    override fun initView() {
        val model = intent.getParcelableExtra<NodeModel>("model")
        mBinding.toolbar.title = model.title

        val nodeName = model.name
        val nodeId = model.id

        val topicFragment = TopicsFragment.newInstance(nodeName,"directOpen")
        supportFragmentManager.beginTransaction().add(R.id.framelayout,topicFragment).commit()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_node
    }

}