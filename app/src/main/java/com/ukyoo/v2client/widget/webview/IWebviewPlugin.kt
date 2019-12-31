package com.ukyoo.v2client.widget.webview

import androidx.lifecycle.DefaultLifecycleObserver


abstract class IWebviewPlugin : DefaultLifecycleObserver, IPluginLifeCycle,
    IWebviewCallback, IBridgeHandler