package com.ukyoo.v2client.ui.detail

import android.os.Build
import android.webkit.WebSettings
import android.webkit.WebView
import com.ukyoo.v2client.R
import com.ukyoo.v2client.base.BaseActivity
import com.ukyoo.v2client.databinding.ActivityDetailBinding
import android.webkit.WebViewClient



class DetailActivity : BaseActivity<ActivityDetailBinding>() {
    override fun loadData(isRefresh: Boolean) {
    }

    override fun initView() {
        val web_view = mBinding.webview


        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.getSettings().setDomStorageEnabled(true);
        web_view.getSettings().setUseWideViewPort(true);
        web_view.getSettings().setLoadWithOverviewMode(true);
        web_view.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            web_view.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (Build.VERSION.SDK_INT >= 19) { //对加载的优化
            web_view.getSettings().setLoadsImagesAutomatically(true);
        } else {
            web_view.getSettings().setLoadsImagesAutomatically(false);
        }
//解决漏洞
        web_view.removeJavascriptInterface("searchBoxJavaBridge_");
        web_view.removeJavascriptInterface("accessibility");
        web_view.removeJavascriptInterface("accessibilityTraversal");
//        web_view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置 缓存模式
        web_view.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); //滑动条的样式
        web_view.setHorizontalScrollBarEnabled(false);  //取消Horizontal ScrollBar显示


        web_view.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return super.shouldOverrideUrlLoading(view, url)
            }
        })


        web_view.loadUrl("http://m.sh.centanet.com/shangye/?utm_source=wap")

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_detail
    }

}