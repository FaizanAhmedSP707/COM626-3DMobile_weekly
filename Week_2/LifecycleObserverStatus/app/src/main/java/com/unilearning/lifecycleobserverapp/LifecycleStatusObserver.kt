package com.unilearning.lifecycleobserverapp

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LifecycleStatusObserver(val lifeViewModel: LifecycleViewModel): DefaultLifecycleObserver {
    private var message = false
    override fun onCreate(owner: LifecycleOwner) {
        // When the activity is first launched, this will run first
        message = true
        lifeViewModel.addStatusMessage("Started")
    }

    override fun onStart(owner: LifecycleOwner) {
        // Before onResume is called and after onCreate is executed
        lifeViewModel.addStatusMessage("Started")
    }

    override fun onPause(owner: LifecycleOwner) {
        // When the activity is paused
        message = false
        lifeViewModel.addStatusMessage("Paused")
    }

    override fun onResume(owner: LifecycleOwner) {
        // When the activity is resumed again after another activity or app was closed when this app was running
        message = false
        lifeViewModel.addStatusMessage("Resumed")
    }

    override fun onStop(owner: LifecycleOwner) {
        // Before onDestroy is called
        lifeViewModel.addStatusMessage("Stopped")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        /*
        When the application is destroyed. This doesn't mean that you have to close the app completely
        to see this message, and obviously this message won't show up in that case. If the orientation of
        the device is changed, the current application will be destroyed and created anew. That is when
        this message will be seen
        */
        message = false
        lifeViewModel.addStatusMessage("Destroyed")
    }
}