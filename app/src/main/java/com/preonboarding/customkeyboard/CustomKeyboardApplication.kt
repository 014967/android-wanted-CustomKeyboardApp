package com.preonboarding.customkeyboard

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * @Created by 김현국 2022/10/12
 */
@HiltAndroidApp
class CustomKeyboardApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
