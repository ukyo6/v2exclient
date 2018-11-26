package com.ukyoo.v2client.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment

class MeFragment: Fragment() {
    companion object {
        fun newInstance(bundle: Bundle):MeFragment{
            val meFragment = MeFragment()
            meFragment.arguments = bundle
            return meFragment
        }
    }



}