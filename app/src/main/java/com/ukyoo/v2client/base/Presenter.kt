package com.ukyoo.v2client.base

import android.os.Bundle


/**
 * created by hewei
 */
interface Presenter{

    fun loadData(isRefresh:Boolean, savedInstanceState: Bundle?)
}