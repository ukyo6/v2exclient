package com.ukyoo.v2client.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.github.chrisbanes.photoview.PhotoView
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseActivity
import com.ukyoo.v2client.databinding.ActivityPhotobrowseBinding
import com.ukyoo.v2client.util.GlideApp
import com.ukyoo.v2client.util.SizeUtils

class PhotoBrowseActivity : BaseActivity<ActivityPhotobrowseBinding>() {

    companion object {
        private const val IMAGES = "IMAGES"
        private const val POSITION = "POSITION"

        fun loadPhotos(context: Context, position: Int, imageUrls: ArrayList<String>) {
            val intent = Intent(context, PhotoBrowseActivity::class.java)
            intent.putExtra(IMAGES, imageUrls)
            intent.putExtra(POSITION, position)
            context.startActivity(intent)
        }
    }

    private lateinit var mPhohoUrls: ArrayList<String>
    private var mPosition: Int = 0

    override fun loadData(isRefresh: Boolean, savedInstanceState: Bundle?) {

    }

    override fun initView() {
        if (intent.hasExtra(IMAGES)) {
            mPhohoUrls = intent.getSerializableExtra(IMAGES) as ArrayList<String>
        }

        if (intent.hasExtra(POSITION)) {
            mPosition = intent.getIntExtra(POSITION, 0)
        }

        mBinding.viewpager.apply {
            adapter = ImgAdapter()
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }

                override fun onPageSelected(position: Int) {
                    mBinding.toolbar.title = "图片(" + (position + 1) + "/" + mPhohoUrls.size + ")"
                }
            })
        }


        mBinding.toolbar.title = "图片(" + (mPosition + 1) + "/" + mPhohoUrls.size + ")"
        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mBinding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }


    internal inner class ImgAdapter : PagerAdapter() {
        override fun getCount(): Int {
            return mPhohoUrls.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val photoView = PhotoView(container.context)
            photoView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                SizeUtils.dp2px(mContext, 251F)
            )

            GlideApp.with(mContext)
                .load(mPhohoUrls[position])
                .placeholder(R.drawable.ic_default_img)
                .error(R.drawable.ic_default_img)
                .into(photoView)

            container.addView(photoView)
            return photoView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_photobrowse
    }

}