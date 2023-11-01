package com.unilearning.lifecycleobserverapp

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LifecycleStatusObserver(val lifeViewModel: LifecycleViewModel): DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {

    }

    override fun onPause(owner: LifecycleOwner) {

    }

    override fun onResume(owner: LifecycleOwner) {

    }

    override fun onDestroy(owner: LifecycleOwner) {

    }
}