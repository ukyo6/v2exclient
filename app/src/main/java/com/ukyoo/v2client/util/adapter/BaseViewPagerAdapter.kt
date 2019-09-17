package com.ukyoo.v2client.util.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

abstract class BaseViewPagerAdapter(fm: FragmentManager, var title: Array<String>) : FragmentStatePagerAdapter(fm) {
    var list :MutableList<Fragment?> = mutableListOf()

    init {
        title.iterator().forEach { list.add(null) }
    }

    override fun getCount(): Int = title.size

    abstract override fun getItem(pos: Int): Fragment?

    override fun getPageTitle(position: Int): CharSequence = title[position]

}