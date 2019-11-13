package com.ukyoo.v2client.ui.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.ukyoo.v2client.R

class DetailActivity1111 : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acti111)

        val ints = IntArray(120000000)

        findViewById<Button>(R.id.btn).setOnClickListener {
            startActivity(Intent(this, DetailActivity1111::class.java))
        }
    }
}