package com.ukyoo.v2client.ui.detail

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseActivity
import com.ukyoo.v2client.databinding.ActivityDetailBinding
import com.ukyoo.v2client.entity.ReplyModel
import com.ukyoo.v2client.entity.TopicModel
import com.ukyoo.v2client.inter.ItemClickPresenter
import com.ukyoo.v2client.ui.viewmodels.DetailViewModel
import com.ukyoo.v2client.util.adapter.SingleTypeAdapter
import com.ukyoo.v2client.util.bindLifeCycle


class DetailActivity : BaseActivity<ActivityDetailBinding>(),ItemClickPresenter<ReplyModel>  {
    override fun onItemClick(v: View?, item: ReplyModel) {


    }

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

            viewModel.getRepliesByTopicId(true)
                .bindLifeCycle(this)
                .subscribe({

                },{
                    toastFailure(it)
                })
        } else if (intent.getParcelableExtra<TopicModel>("model") != null) {
            model = intent.getParcelableExtra("model")
            mTopicId = model.id

            viewModel.getTopicAndRepliesByTopicId(true)
        }
    }

    override fun initView() {
        mBinding.recyclerview.run {
            layoutManager = LinearLayoutManager(mContext)
            adapter = SingleTypeAdapter(mContext, R.layout.item_reply, viewModel.replyList).apply {
                itemPresenter = this@DetailActivity
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_detail
    }
}