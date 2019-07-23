package com.ukyoo.v2client.ui.detail

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.ukyoo.v2client.BR
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseActivity
import com.ukyoo.v2client.data.entity.ReplyModel
import com.ukyoo.v2client.data.entity.V2EXModel
import com.ukyoo.v2client.databinding.ActivityDetailBinding
import com.ukyoo.v2client.inter.ItemClickPresenter
import com.ukyoo.v2client.inter.RetryCallback
import com.ukyoo.v2client.util.InputUtils
import com.ukyoo.v2client.util.ToastUtil
import com.ukyoo.v2client.util.adapter.DetailAdapter
import com.ukyoo.v2client.widget.EnterLayout
import java.util.ArrayList

/**
 *  详情页
 */
class DetailActivity : BaseActivity<ActivityDetailBinding>(), ItemClickPresenter<Any> {
    private var mTopicId: Int = 0


    companion object {
        const val ITEM_TOPIC = 1
        const val ITEM_REPLY = 2
    }

    //get viewModel by di
    private val viewModel by lazy {
        getInjectViewModel<DetailViewModel>()
    }

    override fun restoreArgs(savedInstanceState: Bundle?) {
        super.restoreArgs(savedInstanceState)
        mTopicId = savedInstanceState?.getInt("topic_id", -1) ?: -1
    }

    override fun saveArgs(outState: Bundle?) {
        super.saveArgs(outState)
        mTopicId.let { outState?.putInt("topic_id", it) }
    }

    var isJsonApi: Boolean = false

    override fun loadData(isRefresh: Boolean, savedInstanceState: Bundle?) {
        //设置topicId
        if (intent.hasExtra("topic_id")) {
            mTopicId = intent.getIntExtra("topic_id", -1)
            viewModel.setTopicId(mTopicId)
        }

        val mDetailAdapter = DetailAdapter(emptyList())
        viewModel.topicAndReplies.observe(this@DetailActivity, Observer { resource ->//观察者

            if(resource.data == null){
                mDetailAdapter.setNewData(emptyList())
            } else {
                val topicInfo = resource.data.topicInfo
                val replies = resource.data.replies
                val list = ArrayList<MultiItemEntity>()
                list.add(topicInfo as MultiItemEntity)
                list.addAll(replies as List<MultiItemEntity>)
                mDetailAdapter.setNewData(list)
            }
        })

        //回复列表
        mBinding.recyclerview.run {
            layoutManager = LinearLayoutManager(mContext)
            adapter = mDetailAdapter
        }
    }


    private lateinit var mEnterLayout: EnterLayout

    override fun initView() {
        getComponent().inject(this)
        mBinding.setVariable(BR.vm, viewModel)

        mEnterLayout = EnterLayout(mContext, mBinding.root, onClickSend)
        mEnterLayout.setDefaultHint("评论主题")
//        mEnterLayout.hide()


        //重试的回调
        mBinding.retryCallback = object : RetryCallback {
            override fun retry() {
                viewModel.retry()
            }
        }
    }

    private var onClickSend: View.OnClickListener = View.OnClickListener {
        val content = mEnterLayout.getContent()
        if (content.isEmpty()) {
            ToastUtil.shortShow("回复内容不能为空")
            return@OnClickListener
        }
        InputUtils.popSoftkeyboard(mContext, mEnterLayout.content, false)

        viewModel.getReplyOnce(mTopicId)
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_detail
    }

    override fun onItemClick(v: View?, item: Any) {
        if (item is ReplyModel) {
            //回复
            prepareAddComment(item, true)
        }
    }

    private fun prepareAddComment(data: V2EXModel, popKeyboard: Boolean) {
        val content = mEnterLayout.content
        var replyToWho = ""
        mEnterLayout.clearContent()
        if (data is ReplyModel) {
            val replyObj = data as ReplyModel
            content.hint = "回复 " + replyObj.member.username
            replyToWho = "@" + replyObj.member.username + " "
        }

        if (popKeyboard) {
            InputUtils.popSoftkeyboard(mContext, content, true)
            Handler().postDelayed({
                content.setText(replyToWho)
                content.setSelection(content.text.length)
            }, 500)
        } else {
            InputUtils.popSoftkeyboard(mContext, content, false)
        }
    }
}