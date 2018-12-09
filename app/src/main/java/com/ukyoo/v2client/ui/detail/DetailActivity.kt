package com.ukyoo.v2client.ui.detail

import android.os.Bundle
import android.os.PersistableBundle
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseActivity
import com.ukyoo.v2client.databinding.ActivityDetailBinding


class DetailActivity : BaseActivity<ActivityDetailBinding>() {
    private var mTopicId:Int = 0

    override fun restoreArgs(savedInstanceState: Bundle?) {
        super.restoreArgs(savedInstanceState)
    }

    override fun saveArgs(outState: Bundle?) {
        super.saveArgs(outState)
        outState?.putInt("topic_id", mTopicId)
    }

    override fun loadData(isRefresh: Boolean) {
    }

    override fun initView() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_detail
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
    }
}