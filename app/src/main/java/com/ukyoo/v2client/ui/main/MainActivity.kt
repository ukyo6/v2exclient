package com.ukyoo.v2client.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ukyoo.v2client.R

/**
 * 主页
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
