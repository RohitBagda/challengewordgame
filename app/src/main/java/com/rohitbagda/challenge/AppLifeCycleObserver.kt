package com.rohitbagda.challenge

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class AppLifecycleObserver(private val viewModel: ChallengeViewModel) : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        viewModel.isAppInBackground = false
        super.onCreate(owner)
    }

    override fun onStart(owner: LifecycleOwner) {
        viewModel.isAppInBackground = false
        super.onStart(owner)
    }

    override fun onResume(owner: LifecycleOwner) {
        viewModel.isAppInBackground = false
        super.onResume(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        viewModel.isAppInBackground = true
        super.onResume(owner)
    }

    override fun onStop(owner: LifecycleOwner) {
        viewModel.isAppInBackground = true
        super.onStop(owner)
    }
}