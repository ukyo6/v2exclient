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
import com.ukyoo.v2client.base.BaseFragment
import com.ukyoo.v2client.databinding.FragmentMainChildBinding
import com.ukyoo.v2client.di.scope.FragmentScope
import com.ukyoo.v2client.viewmodels.MainChildViewModel

@FragmentScope
class MainChildFragment : BaseFragment<FragmentMainChildBinding>() {

    companion object {
        fun newInstance(bundle: Bundle): MainChildFragment {
            val fragment = MainChildFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private val adapter:BaseQuickAdapter<String, BaseViewHolder> by lazy{
        object: BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_main_child) {
            override fun convert(helper: BaseViewHolder, item: String) {

            }
        }
    }

    override fun initView() {
        //设置adapter
        mBinding.recyclerview.layoutManager = LinearLayoutManager(activity)
        mBinding.recyclerview.adapter = adapter
        //更新界面
        updateUi(adapter)
    }

    override fun loadData(isRefresh: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_main_child
    }

    private lateinit var viewModel: MainChildViewModel


    /**
     * 更新界面
     */
    private fun updateUi(adapter: BaseQuickAdapter<String, BaseViewHolder>) {
//        viewModel.getData().observe(viewLifecycleOwner, Observer { dts ->
//            dts ?: adapter.setNewData(dts)
//        })
        adapter.setNewData(null)
    }
}