package com.ukyoo.v2client.ui.detail

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.ukyoo.v2client.BR
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseActivity
import com.ukyoo.v2client.data.Status
import com.ukyoo.v2client.databinding.ActivityDetailBinding
import com.ukyoo.v2client.data.entity.ReplyItem
import com.ukyoo.v2client.data.entity.TopicInfo
import com.ukyoo.v2client.inter.RetryCallback
import com.ukyoo.v2client.ui.userinfo.UserInfoActivity
import com.ukyoo.v2client.util.InputUtils
import com.ukyoo.v2client.util.ToastUtil
import com.ukyoo.v2client.util.adapter.DetailAdapter
import com.ukyoo.v2client.widget.EnterLayout
import java.util.*

/**
 *  详情页
 */
class DetailActivity : BaseActivity<ActivityDetailBinding>() {
    private var mTopicId: Int? = null

    private val mDetailAdapter by lazy { DetailAdapter(emptyList()) }


    companion object {
        const val ITEM_TOPIC = 1
        const val ITEM_REPLY = 2
    }

    private val viewModel by lazy { getInjectViewModel<DetailViewModel>() }

    override fun initView() {
        getComponent().inject(this)
        mBinding.setVariable(BR.vm, viewModel)

        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mEnterLayout = EnterLayout(mContext, mBinding.root, onClickSend)
        mEnterLayout.setDefaultHint("评论主题")
//        mEnterLayout.hide()
    }

    override fun loadData(isRefresh: Boolean, savedInstanceState: Bundle?) {
        //设置topicId
        if (intent.hasExtra("topic_id")) {
            mTopicId = intent.getIntExtra("topic_id", -1)
            viewModel.setTopicId(mTopicId)
        }

        //重试的回调
        mBinding.retryCallback = object : RetryCallback {
            override fun retry() {
                viewModel.refreshDetail()
            }
        }

        //回复列表
        mBinding.recyclerview.run {
            layoutManager = LinearLayoutManager(mContext)
            adapter = mDetailAdapter
        }

        mDetailAdapter.setOnItemClickListener { adapter, _, position ->
            val item = adapter.data[position]
            if (item is ReplyItem) {
                //回复
                prepareAddComment(item, true)
            }
        }

        mDetailAdapter.setOnItemChildClickListener { adapter, view, position ->
            //查看个人信息
            if (view.id == R.id.iv_avatar) {
                val item = adapter.data[position]

                var userName = ""
                when (item) {
                    is ReplyItem -> userName = item.member?.username ?: ""
                    is TopicInfo -> userName = item.member?.username ?: ""
                }
                val intent = Intent(mContext, UserInfoActivity::class.java)
                intent.putExtra("username", userName)
                startActivity(intent)
            }
        }


        subscribeUi()
    }

    private fun subscribeUi() {
        //观察列表数据
        viewModel.topicAndReplies.observe(this@DetailActivity, Observer { resource ->

            if (resource?.data == null) {
                mDetailAdapter.setNewData(emptyList())
            } else {
                val topicInfo = resource.data.topicInfo
                val replies = resource.data.replies
                mBinding.toolbar.title = topicInfo?.node?.title //title

                val list = ArrayList<MultiItemEntity>().apply {
                    add(topicInfo as MultiItemEntity)
                    addAll(replies as List<MultiItemEntity>)
                }
                mDetailAdapter.setNewData(list)
            }
        })

        //观察回复结果
        viewModel.replyResult.observe(this@DetailActivity, Observer {
            when (it.status) {
                Status.SUCCESS -> ToastUtil.shortShow("回复成功")
                Status.ERROR -> ToastUtil.shortShow(it.message)
                Status.LOADING -> {
                }
            }
        })
    }


    private lateinit var mEnterLayout: EnterLayout


    private var onClickSend: View.OnClickListener = View.OnClickListener {
        val content = mEnterLayout.getContent()
        if (content.isEmpty()) {
            ToastUtil.shortShow("回复内容不能为空")
            return@OnClickListener
        }
        InputUtils.popSoftkeyboard(mContext, mEnterLayout.content, false)

        viewModel.reply(mTopicId, content)
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_detail
    }

    private fun prepareAddComment(data: ReplyItem, popKeyboard: Boolean) {
        val content = mEnterLayout.content
        var replyToWho = ""
        mEnterLayout.clearContent()


        content.hint = "回复 " + data.member?.username
        replyToWho = "@" + data.member?.username + " "

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