package com.ukyoo.v2client.ui.main

import androidx.fragment.app.Fragment

class MainChildFragment : Fragment() {
//class MainChildFragment : BaseFragment<FragmentMainChildBinding>() {
//
//    companion object {
//        fun newInstance(bundle: Bundle): MainChildFragment {
//            val fragment = MainChildFragment()
//            fragment.arguments = bundle
//            return fragment
//        }
//    }
//
//    private val adapter:BaseQuickAdapter<String, BaseViewHolder> by lazy{
//        object: BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_main_child) {
//            override fun convert(helper: BaseViewHolder, item: String) {
//
//            }
//        }
//    }
//
//    override fun initView() {
//        //设置adapter
//        mBinding.recyclerview.layoutManager = LinearLayoutManager(activity)
//        mBinding.recyclerview.adapter = adapter
//        //更新界面
//        updateUi(adapter)
//    }
//
//    override fun loadData(isRefresh: Boolean) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun getLayoutId(): Int {
//        return R.layout.fragment_main_child
//    }
//
//    private lateinit var viewModel: MainChildViewModel
//
//
//    /**
//     * 更新界面
//     */
//    private fun updateUi(adapter: BaseQuickAdapter<String, BaseViewHolder>) {
////        viewModel.getData().observe(viewLifecycleOwner, Observer { dts ->
////            dts ?: adapter.setNewData(dts)
////        })
//        adapter.setNewData(null)
//    }
}