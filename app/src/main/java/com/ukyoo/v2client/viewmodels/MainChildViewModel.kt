package com.ukyoo.v2client.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainChildViewModel: ViewModel() {

    private var list = MutableLiveData<List<String>>()

    init {


    }







    fun getData() = list
}