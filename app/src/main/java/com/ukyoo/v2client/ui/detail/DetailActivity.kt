package com.ukyoo.v2client.ui.detail

import android.os.Bundle
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseActivity
import com.ukyoo.v2client.databinding.ActivityDetailBinding
import com.ukyoo.v2client.entity.TopicModel
import com.ukyoo.v2client.ui.viewmodels.DetailViewModel


class DetailActivity : BaseActivity<ActivityDetailBinding>() {
    private var mTopicId: Int? = 0
    private lateinit var model: TopicModel

    //get viewModel by di
    private val viewModel by lazy {
        getInjectViewModel<DetailViewModel>()
    }

    override fun restoreArgs(savedInstanceState: Bundle?) {
        super.restoreArgs(savedInstanceState)
        mTopicId = savedInstanceState?.getInt("topic_id",-1)
    }

    override fun saveArgs(outState: Bundle?) {
        super.saveArgs(outState)
        mTopicId?.let { outState?.putInt("topic_id", it) }
    }

    override fun loadData(isRefresh: Boolean) {
        if (intent.getStringExtra("topic_id") != null) {
            mTopicId = intent.getIntExtra("topic_id", -1)

            viewModel.getReplies()
        } else if (intent.getParcelableExtra<TopicModel>("model") != null) {
            model = intent.getParcelableExtra("model")
            mTopicId = model.id

            viewModel.getTopicAndReplies()
        }



    }

    override fun initView() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_detail
    }
}