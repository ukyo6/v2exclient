package com.ukyoo.v2client.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ukyoo.v2client.R
import com.ukyoo.v2client.databinding.FragmentMainChildBinding
import com.ukyoo.v2client.viewmodels.MainChildViewModel

class MainChildFragment : Fragment() {

    private lateinit var viewModel: MainChildViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var binding =
            DataBindingUtil.inflate<FragmentMainChildBinding>(inflater, R.layout.fragment_main_child, container, false)

        //获得viewModel
        viewModel = ViewModelProviders.of(this@MainChildFragment).get(MainChildViewModel::class.java)

        //设置adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(activity)
        var adapter = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_main_child) {
            override fun convert(helper: BaseViewHolder, item: String) {

            }
        }
        binding.recyclerview.adapter = adapter
        //更新界面
        updateUi(adapter)
        return binding.root
    }

    /**
     * 更新界面
     */
    private fun updateUi(adapter: BaseQuickAdapter<String, BaseViewHolder>) {
        viewModel.getData().observe(viewLifecycleOwner, Observer { dts ->
            dts ?: adapter.setNewData(dts)
        })
    }
}