package com.ukyoo.v2client.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.ukyoo.v2client.databinding.FragmentMainBinding

/**
 * 主页
 */
class MainFragment : Fragment() {

    var list: List<Fragment> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.tablayout.setupWithViewPager(binding.viewpager)
        binding.viewpager.adapter = object : FragmentPagerAdapter(fragmentManager) {
            override fun getCount(): Int {
                return list.size
            }

            override fun getItem(position: Int): Fragment {
                return list[position]
            }
        }
        subscribeUI(binding.viewpager.adapter, binding)
        return binding.root
    }

    private fun subscribeUI(
        adapter: PagerAdapter?,
        binding: FragmentMainBinding
    ) {


    }
}