package com.ukyoo.v2client.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.databinding.FragmentNodesBinding
import com.ukyoo.v2client.data.entity.NodeModel
import com.ukyoo.v2client.inter.ItemClickPresenter
import com.ukyoo.v2client.ui.node.NodeActivity
import com.ukyoo.v2client.ui.node.NodesViewModel
import com.ukyoo.v2client.util.adapter.SingleTypeAdapter

/**
 * @desc 所有节点
 * @author hewei
 */
class NodesFragment : BaseFragment<FragmentNodesBinding>(), ItemClickPresenter<NodeModel> {
    override fun isLazyLoad(): Boolean {
        return true
    }

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

    override fun initView() {
        getComponent().inject(this)

        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(mBinding.toolbar)

        mBinding.vm = viewModel
        mBinding.recyclerview.run {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = SingleTypeAdapter(mContext, R.layout.item_node, viewModel.nodesList).apply {
                itemPresenter = this@NodesFragment
            }
        }

    }

    override fun loadData(isRefresh: Boolean, savedInstanceState: Bundle?) {
        viewModel.loadData()
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_nodes
    }

    /**
     * item click
     */
    override fun onItemClick(v: View?, item: NodeModel) {
        val intent = Intent(mContext, NodeActivity::class.java)
        intent.putExtra("model", item as Parcelable)
        mContext.startActivity(intent)
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
                newText?.let { viewModel.queryByName(it) }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }
}