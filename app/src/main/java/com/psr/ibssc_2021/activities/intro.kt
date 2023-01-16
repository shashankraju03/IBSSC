package com.psr.ibssc_2021.activities

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.psr.ibssc_2021.R
class intro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        //val typeface: Typeface= Typeface.createFromAsset(assets,"Summer Calling.ttf")
        val sign_up_intro= findViewById<Button>(R.id.btn_sign_up_intro)
        sign_up_intro.setOnClickListener(){
            val intent= Intent(this@intro, sign_up::class.java)
            startActivity(intent)
        }
        val sign_in_intro= findViewById<Button>(R.id.btn_sign_in_intro)
        sign_in_intro.setOnClickListener(){
            val intent= Intent(this@intro, sign_in::class.java)
            startActivity(intent)
        }

    }


}