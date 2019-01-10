package com.ukyoo.v2client.base.viewmodel

import androidx.lifecycle.ViewModel

/**
 * 警告：ViewModel绝不能引用视图，生命周期或可能持有对活动上下文的引用的任何类。
 */
open class BaseViewModel: ViewModel()