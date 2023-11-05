package com.unilearning.lifecycleobserverapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {

    val lifeViewModel: LifecycleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val observer = LifecycleStatusObserver(lifeViewModel)
        this.lifecycle.addObserver(observer)

        lifeViewModel.statesLive.observe(this, Observer{
            findViewById<TextView>(R.id.tvStateLister).movementMethod = ScrollingMovementMethod()
            findViewById<TextView>(R.id.tvStateLister).text = it.joinToString("\n")
        })
    }
}