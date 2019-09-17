package com.ukyoo.v2client.ui.main

import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.orhanobut.logger.Logger
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseActivity
import com.ukyoo.v2client.databinding.ActivityMainBinding
import com.ukyoo.v2client.util.ToastUtil
import com.ukyoo.v2client.util.async
import com.ukyoo.v2client.util.bindLifeCycle
import io.reactivex.Single


/**
 * 主页
 */

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun loadData(isRefresh: Boolean, savedInstanceState: Bundle?) {
    }

    private var isQuit = false

    override fun initView() {
        getComponent().inject(this)

        //初始化viewpager
        val fragments = ArrayList<Fragment>()
        fragments.add(HomeFragment.newInstance())
        fragments.add(NodesFragment.newInstance())
        fragments.add(ProfilerFragment.newInstance())

        mBinding.viewpager.run {
            adapter = object: FragmentStatePagerAdapter(supportFragmentManager){
                override fun getItem(position: Int): Fragment {
                    return fragments[position]
                }

                override fun getCount(): Int {
                    return fragments.size
                }

                override fun instantiateItem(container: ViewGroup, position: Int): Any {
                    return super.instantiateItem(container, position)
                }

//                override fun getItemPosition(`object`: Any): Int {
//                    return POSITION_NONE
//                }
            }
            offscreenPageLimit = fragments.size - 1
        }

        mBinding.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> mBinding.navigationView.selectedItemId = R.id.nav_home  //主页
                    1 -> mBinding.navigationView.selectedItemId = R.id.nav_repos //节点
                    2 -> mBinding.navigationView.selectedItemId = R.id.nav_profile //个人
                }
            }
        })

        mBinding.navigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    mBinding.viewpager.currentItem = 0
                }
                R.id.nav_repos -> {
                    mBinding.viewpager.currentItem = 1
                }
                R.id.nav_profile -> {
                    mBinding.viewpager.currentItem = 2
                }
            }
            true
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onBackPressed() {
        if (!isQuit) {
            ToastUtil.shortShow("再按一次退出程序")
            isQuit = true
            //在两秒钟之后isQuit会变成false
            Single.just(isQuit)
                .async(2000)
                .bindLifeCycle(this)
                .subscribe({ isQuit = false }, {})
        } else {
            super.onBackPressed()
        }
    }

}
