package com.ukyoo.v2client.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.core.view.MenuItemCompat
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.databinding.FragmentNodesBinding

class NodesFragment : BaseFragment<FragmentNodesBinding>(){

    companion object {
        fun newInstance(bundle: Bundle):NodesFragment{
            val nodesFragment = NodesFragment()
            nodesFragment.arguments = bundle
            return nodesFragment
        }
    }

    override fun initView() {
        setHasOptionsMenu(true)
    }

    override fun loadData(isRefresh: Boolean) {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_nodes
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {


        inflater?.inflate(R.menu.menu_search_view,menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val actionView = searchItem?.actionView

        super.onCreateOptionsMenu(menu, inflater)

    }


}