package com.ukyoo.v2client.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.databinding.FragmentNodesBinding
import com.ukyoo.v2client.ui.node.NodeActivity
import com.ukyoo.v2client.ui.node.NodesViewModel
import com.ukyoo.v2client.util.adapter.NodesAdapter

/**
 * @desc 所有节点
 * @author hewei
 */
class NodesFragment : BaseFragment<FragmentNodesBinding>() {

    private val mNodesAdapter by lazy { NodesAdapter(R.layout.item_node) }


    override fun isLazyLoad(): Boolean {
        return true
    }

    //get viewModel by di
    private val viewModel by lazy {
        getInjectViewModel<NodesViewModel>()
    }

    companion object {
        fun newInstance(): NodesFragment {
            return NodesFragment()
        }
    }

    private fun initView() {
        getComponent().inject(this)
        mBinding.vm = viewModel

        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(mBinding.toolbar)

        mBinding.recyclerview.run {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = mNodesAdapter
        }

        mNodesAdapter.setOnItemClickListener { adapter, _, position ->
            val item = adapter.data[position]
            val intent = Intent(mContext, NodeActivity::class.java)
            intent.putExtra("model", item as Parcelable)
            mContext.startActivity(intent)
        }
    }

    override fun loadData(isRefresh: Boolean) {
        initView()

        viewModel.setQueryName("")
        subscribeUi()
    }

    private fun subscribeUi() {
        viewModel.nodesLiveData.observe(this@NodesFragment, Observer {
            if (it == null) {
                mNodesAdapter.setNewData(emptyList())
            } else {
                mNodesAdapter.setNewData(it.data)
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