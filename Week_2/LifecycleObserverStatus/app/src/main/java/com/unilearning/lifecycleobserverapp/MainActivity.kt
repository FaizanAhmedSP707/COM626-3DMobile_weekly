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

        // accessing our textview here first so that we can access it later
        val tv = findViewById<TextView>(R.id.tvStateLister)
        // adding the movement option prescribed to the text view by calling the appropriate function
        tv.movementMethod = ScrollingMovementMethod()

        val observer = LifecycleStatusObserver(lifeViewModel)
        /*
        The lifecycle keyword here is an attribute of the main activity. We create our observer
        object first, then pass it as an argument to the attribute's "addObserver" method so that
        it can monitor the changes in the lifecycle of this app, and thus pass the right status
        message to the list inside the ViewModel.
        */
        this.lifecycle.addObserver(observer)

        /*
        This observer is different to the lifecycle observer attached to the app, in that it is
        simply monitoring changes to the live version of the list inside the viewModel, and so
        when a change is noticed in the live data, the new strings added to the ViewModel are then
        displayed on screen by appending to the new string to the old string using the new line
        separator (\n).
        */
        lifeViewModel.statesLive.observe(this, Observer{
            tv.text = it.joinToString("\n")
        })
    }
}