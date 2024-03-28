package com.example.tablerichtext.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tablerichtext.App
import com.example.tablerichtext.R
import com.example.tablerichtext.mathweb.MathView

class WebActivityRt2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webrt2)
        this.findViewById<MathView>(R.id.math_view)
            .setDisplayText(App.getSample())
    }
}