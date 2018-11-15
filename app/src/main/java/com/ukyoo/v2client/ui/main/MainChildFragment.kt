package com.ukyoo.v2client.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.ukyoo.v2client.R
import com.ukyoo.v2client.databinding.FragmentMainChildBinding

class MainChildFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var binding = DataBindingUtil.inflate<FragmentMainChildBinding>(inflater, R.layout.fragment_main_child, container, false).apply {


        }
        return binding.root

//       return  inflater.inflater(inflater,R.layout.fragment_main_child,container,false)
//        recyclerview.layoutManager = LinearLayoutManager(this@MainChildFragment.activity)
//        recyclerview.adapter = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.fragment_main_child) {
//            override fun convert(helper: BaseViewHolder, item: String) {
//
//            }
//        }
    }


}