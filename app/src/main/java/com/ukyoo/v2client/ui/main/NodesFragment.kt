package com.ukyoo.v2client.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment

class NodesFragment : Fragment(){

    companion object {
        fun newInstance(bundle: Bundle):NodesFragment{
            val nodesFragment = NodesFragment()
            nodesFragment.arguments = bundle
            return nodesFragment
        }
    }

}