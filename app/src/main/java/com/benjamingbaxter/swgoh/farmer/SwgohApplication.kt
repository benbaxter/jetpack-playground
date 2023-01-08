package com.benjamingbaxter.swgoh.farmer

import android.app.Application
import timber.log.Timber

class SwgohApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
  }
}