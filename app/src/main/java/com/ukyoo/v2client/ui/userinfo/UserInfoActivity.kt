package com.ukyoo.v2client.ui.userinfo

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukyoo.v2client.BR
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseActivity
import com.ukyoo.v2client.databinding.ActivityUserinfoBinding
import com.ukyoo.v2client.entity.MemberModel
import com.ukyoo.v2client.entity.TopicModel
import com.ukyoo.v2client.inter.ItemClickPresenter
import com.ukyoo.v2client.ui.detail.DetailActivity
import com.ukyoo.v2client.util.adapter.SingleTypeAdapter
import com.ukyoo.v2client.viewmodels.UserInfoViewModel

/**
 * 用户信息
 */
class UserInfoActivity : BaseActivity<ActivityUserinfoBinding>(), ItemClickPresenter<TopicModel> {

    private lateinit var mMember: MemberModel
    private var mUsername: String = ""

    //get viewModel by di
    private val viewModel by lazy {
        getInjectViewModel<UserInfoViewModel>()
    }


    override fun initView() {
        getComponent().inject(this)
        mBinding.setVariable(BR.vm, viewModel)

//        mBinding.recyclerview.run {
//            layoutManager = LinearLayoutManager(mContext)
//            adapter = SingleTypeAdapter(mContext, R.layout.item_topic, viewModel.createdTopics).apply {
//                itemPresenter = this@UserInfoActivity
//            }
//        }

        mBinding.recyclerview.run {
            layoutManager = LinearLayoutManager(mContext)
            adapter = SingleTypeAdapter(mContext,R.layout.item_user_reply,viewModel.createdReplies)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_userinfo
    }

    override fun loadData(isRefresh: Boolean, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val intent = intent
            val data = intent.data
            val scheme = if (data != null) data.scheme else "" // "http"
            val host = if (data != null) data.host else "" // "www.v2ex.com"
            val params = data?.pathSegments
            if ((scheme == "http" || scheme == "https")
                && (host == "www.v2ex.com" || host == "v2ex.com")
                && params != null && params.size == 2
            ) {
                mUsername = params[1]
                updateTitle(mUsername)
            } else {
                if (intent.hasExtra("model")) {
                    mMember = intent.getParcelableExtra<MemberModel>("model")
                    mUsername = mMember.username
                    updateTitle(mUsername)
                } else {
                    mUsername = intent.getStringExtra("username")
                    updateTitle(mUsername)
                }
            }
        } else {
            mUsername = savedInstanceState.getString("username") ?: ""
        }

        viewModel.getUserInfo(mUsername, isRefresh = true)
    }

    private fun updateTitle(username: String) {
//        mBinding.toolbar.title = username
    }

    override fun onItemClick(v: View?, item: TopicModel) {
        val intent = Intent(mContext, DetailActivity::class.java)
        if (item.content == null || item.contentRendered == null)
            intent.putExtra("topic_id", item.id)
        else
            intent.putExtra("model", item as Parcelable)

        startActivity(intent)
    }
}