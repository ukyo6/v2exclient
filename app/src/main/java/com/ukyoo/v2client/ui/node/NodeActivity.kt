package com.ukyoo.v2client.ui.node

import android.os.Bundle
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseActivity
import com.ukyoo.v2client.databinding.ActivityNodeBinding
import com.ukyoo.v2client.data.entity.NodeModel
import com.ukyoo.v2client.ui.main.TopicListFragment

class NodeActivity : BaseActivity<ActivityNodeBinding>() {
    override fun loadData(isRefresh: Boolean, savedInstanceState: Bundle?) {
    }

    override fun initView() {
        val model = intent.getParcelableExtra<NodeModel>("model")
        mBinding.toolbar.title = model.title

        val name = model.name
        val id = model.id

        val bundle = Bundle()
        bundle.putString(TopicListFragment.NODE_NAME, name)
        val topicFragment = TopicListFragment.newInstance(bundle, "directOpen")
        supportFragmentManager.beginTransaction().add(R.id.framelayout, topicFragment).commit()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_node
    }
}