package com.steve.flowlayoutradiogroup

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.Gravity
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.steve.flowlayoutradiogroup.view.FlowLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addRadioBtnViews()
    }

    private fun addRadioBtnViews() {
        for (i in 0..10) {
            val radioButton = RadioButton(this)
            radioButton.setBackgroundResource(R.drawable.selector_translist_bg)
            radioButton.buttonDrawable = BitmapDrawable()
            radioButton.setPadding(dip2px(20f), dip2px(12f), dip2px(20f), dip2px(12f))
            radioButton.text = "text $i $i $i"
            radioButton.textSize = 12f
            radioButton.gravity = Gravity.CENTER
            radioButton.setTextColor(resources.getColor(R.color.selector_period_color))
            findViewById<FlowLayout>(R.id.flowLayout).addView(radioButton)
        }
    }
}