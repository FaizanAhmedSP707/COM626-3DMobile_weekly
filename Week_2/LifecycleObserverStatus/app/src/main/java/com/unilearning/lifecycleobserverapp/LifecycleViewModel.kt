package com.unilearning.lifecycleobserverapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LifecycleViewModel : ViewModel() {
    var states = mutableListOf<String>()
    val statesLive = MutableLiveData<MutableList<String>>()

    fun addStatusMessage(nState : String) {
        states.add(nState)
        statesLive.value = states
    }
}