package com.example.tablerichtext.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tablerichtext.R
import com.google.android.material.button.MaterialButton

class MainActivityRt2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homert2)

        findViewById<MaterialButton>(R.id.btn_native).setOnClickListener {
            startActivity(Intent(this,NativeActivityRt2::class.java))
        }
        findViewById<MaterialButton>(R.id.btn_webview).setOnClickListener {
            startActivity(Intent(this,WebActivityRt2::class.java))
        }
    }
}