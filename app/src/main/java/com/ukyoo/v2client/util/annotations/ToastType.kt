package com.ukyoo.v2client.util.annotations

import androidx.annotation.IntDef
import com.ukyoo.v2client.util.annotations.ToastType.Companion.ERROR
import com.ukyoo.v2client.util.annotations.ToastType.Companion.NORMAL
import com.ukyoo.v2client.util.annotations.ToastType.Companion.SUCCESS
import com.ukyoo.v2client.util.annotations.ToastType.Companion.WARNING

@IntDef(ERROR, NORMAL, SUCCESS, WARNING)
annotation class ToastType {
    companion object {
        const val ERROR = -2
        const val WARNING = -1
        const val NORMAL = 0
        const val SUCCESS = 1
    }
}