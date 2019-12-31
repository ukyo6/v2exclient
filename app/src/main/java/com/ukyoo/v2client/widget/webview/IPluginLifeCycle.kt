package com.ukyoo.v2client.widget.webview


/**
 * 策略自身被加载和卸载时的生命周期的监听
 */

interface IPluginLifeCycle {
    /**
     * 插件被添加的时候调用
     */
    fun onPluginAdded()

    /**
     * 插件被移除的时候调用
     */
    fun onPluginRemoved()
}