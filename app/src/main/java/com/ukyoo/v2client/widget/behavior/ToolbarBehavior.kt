package com.ukyoo.v2client.widget.behavior

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout

class ToolbarBehavior : CoordinatorLayout.Behavior<View>(){


    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return dependency is Toolbar
    }


    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return super.onDependentViewChanged(parent, child, dependency)
    }




}