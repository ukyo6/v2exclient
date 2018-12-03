package com.ukyoo.v2client.ui.main

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ukyoo.v2client.R
import com.ukyoo.v2client.R.id.toolbar
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.databinding.FragmentNodesBinding
import com.ukyoo.v2client.ui.viewmodels.TopicsViewModel
import com.ukyoo.v2client.util.adapter.SingleTypeAdapter

class NodesFragment : BaseFragment<FragmentNodesBinding>() {

    //get viewModel by di
    private val viewModel by lazy {
        getInjectViewModel<TopicsViewModel>()
    }

    companion object {
        fun newInstance(bundle: Bundle): NodesFragment {
            val nodesFragment = NodesFragment()
            nodesFragment.arguments = bundle
            return nodesFragment
        }
    }

    override fun initView() {
        getComponent().inject(this)

        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(mBinding.toolbar)

        mBinding.recyclerview.run {
            layoutManager = LinearLayoutManager(mContext)
            adapter = SingleTypeAdapter(mContext, R.layout.item_topic, viewModel.list)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun loadData(isRefresh: Boolean) {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_nodes
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_search_view, menu)
        //找到searchView
        val search = menu?.findItem(R.id.action_search)
        val actionView = MenuItemCompat.getActionView(search)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_setting -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }
}