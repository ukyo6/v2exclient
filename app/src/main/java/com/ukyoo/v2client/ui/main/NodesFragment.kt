package com.ukyoo.v2client.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.data.Status
import com.ukyoo.v2client.data.entity.NodeModel
import com.ukyoo.v2client.databinding.FragmentNodesBinding
import com.ukyoo.v2client.inter.RetryCallback
import com.ukyoo.v2client.ui.node.NodeActivity
import com.ukyoo.v2client.ui.node.NodesViewModel
import com.ukyoo.v2client.util.ToastUtil
import com.ukyoo.v2client.util.adapter.NodesAdapter

/**
 * @desc 所有节点
 * @author hewei
 */
class NodesFragment : BaseFragment<FragmentNodesBinding>() {

    private val nodesAdapter: NodesAdapter by lazy { NodesAdapter(R.layout.item_node) }

    override fun isLazyLoad() = true

    //get viewModel by di
    private val viewModel by lazy {
        getInjectViewModel<NodesViewModel>()
    }

    companion object {
        fun newInstance(bundle: Bundle): NodesFragment {
            val nodesFragment = NodesFragment()
            nodesFragment.arguments = bundle
            return nodesFragment
        }
    }

    private fun initView() {
        getComponent().inject(this)
        mBinding.vm = viewModel
        mBinding.recyclerview.run {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = nodesAdapter
        }

        nodesAdapter.setOnItemClickListener { _, _, position ->
            val item: NodeModel = nodesAdapter.data[position]

            val intent = Intent(mContext, NodeActivity::class.java)
            intent.putExtra("model", item as Parcelable)
            mContext.startActivity(intent)
        }


        //重试的回调
        mBinding.retryCallback = object : RetryCallback {
            override fun retry() {
                viewModel.retry()
            }
        }

        //查询全部节点
        viewModel.setQueryName("")
    }

    override fun loadData(isRefresh: Boolean, savedInstanceState: Bundle?) {
        initView()

        subscribeUi()
    }

    private fun subscribeUi() {
        viewModel.nodesLiveData.observe(this@NodesFragment, Observer {
            when (it?.status) {
                Status.LOADING -> {
                }
                Status.ERROR -> ToastUtil.shortShow(it.message)

                Status.SUCCESS -> {
                    if (it.data != null) {
                        nodesAdapter.setNewData(it.data)
                    } else {
                        nodesAdapter.setNewData(emptyList())
                    }
                }
            }
        })
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_nodes
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_search_view, menu)
        //searchView
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "输入节点名字"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.setQueryName(it) }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }
}