package com.ukyoo.v2client.util


import android.app.Activity
import android.content.Context
import android.content.SharedPreferences


/**
 * Created by hewei
 */

object SPUtils {

    private var spf: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    var SP_NAME = "SP_V2EX"

    fun init(context: Context) {
        if (spf == null) {
            spf = context.getSharedPreferences(SP_NAME, Activity.MODE_PRIVATE)
            editor = spf!!.edit()


        }
    }

    fun setInt(key: String, value: Int) {
        editor!!.putInt(key, value).apply()
    }

    fun getInt(key: String, defVale: Int): Int {
        return spf!!.getInt(key, defVale)
    }

    fun setString(key: String, value: String) {
        editor!!.putString(key, value).apply()
    }

    fun getString(key: String, defVale: String): String? {
        return spf!!.getString(key, defVale)
    }

    fun setBoolean(key: String, value: Boolean) {
        editor!!.putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defVale: Boolean): Boolean {
        return spf!!.getBoolean(key, defVale)
    }

    fun setLong(key: String, value: Long) {
        editor!!.putLong(key, value).apply()
    }

    fun getLong(key: String, value: Long): Long {
        return spf!!.getLong(key, value)
    }

    fun setFloat(key: String, value: Float) {
        editor!!.putFloat(key, value).apply()
    }

    fun getFloat(key: String, defVale: Float): Float {
        return spf!!.getFloat(key, defVale)
    }

    fun setStringSet(key: String, value: Set<String>) {
        editor?.putStringSet(key, value)?.apply()
    }

    fun getStringSet(key: String): MutableSet<String>? {
        return spf?.getStringSet(key, HashSet<String>())
    }
}
