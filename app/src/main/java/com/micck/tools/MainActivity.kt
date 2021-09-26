package com.micck.tools

import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.micck.tools.viewclick.ViewDoubleClickUtil

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.decorView.post {
            // 1、该页面所有view
            hookAllChildView(window.decorView as ViewGroup)
        }

        findViewById<TextView>(R.id.tv_viewclick_hook).setOnClickListener {
            // 2、指定view
            //ViewDoubleClickUtil.hookView(it)
        }

        findViewById<TextView>(R.id.tv_viewclick_aop).setOnClickListener{

        }
    }

    private fun hookAllChildView(viewGroup: ViewGroup) {
        val count = viewGroup.childCount
        for (i in 0 until count) {
            if (viewGroup.getChildAt(i) is ViewGroup) {
                hookAllChildView(viewGroup.getChildAt(i) as ViewGroup)
            } else {
                ViewDoubleClickUtil.hookView(viewGroup.getChildAt(i))
            }
        }
    }
}