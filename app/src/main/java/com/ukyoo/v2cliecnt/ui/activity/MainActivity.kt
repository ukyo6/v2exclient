package com.ukyoo.v2cliecnt.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ukyoo.v2cliecnt.R

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
