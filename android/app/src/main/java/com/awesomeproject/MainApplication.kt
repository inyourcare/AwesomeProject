package com.awesomeproject

import android.app.Application
import android.content.pm.PackageManager
import com.facebook.react.PackageList
import com.facebook.react.ReactApplication
import com.facebook.react.ReactHost
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.load
import com.facebook.react.defaults.DefaultReactHost.getDefaultReactHost
import com.facebook.react.defaults.DefaultReactNativeHost
import com.facebook.soloader.SoLoader

// import android.app.Application
import android.content.Intent
// import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Handler
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
// import com.facebook.react.PackageList
// import com.facebook.react.ReactApplication
// import com.facebook.react.ReactHost
// import com.facebook.react.ReactNativeHost
// import com.facebook.react.ReactPackage
import com.facebook.react.bridge.ReactContext
// import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.load
// import com.facebook.react.defaults.DefaultReactHost.getDefaultReactHost
// import com.facebook.react.defaults.DefaultReactNativeHost
import com.facebook.react.modules.core.DeviceEventManagerModule
// import com.facebook.soloader.SoLoader
// import com.google.android.gms.wearable.DataClient
// import com.google.android.gms.wearable.DataEvent
// import com.google.android.gms.wearable.DataEventBuffer
// import com.google.android.gms.wearable.DataMapItem
// import com.google.android.gms.wearable.Wearable

class MainApplication : Application(), ReactApplication {

  override val reactNativeHost: ReactNativeHost =
      object : DefaultReactNativeHost(this) {
        override fun getPackages(): List<ReactPackage> =
            PackageList(this).packages.apply {
              // Packages that cannot be autolinked yet can be added manually here, for example:
              // add(MyReactNativePackage())
              add(MainAppPackage());
          }

        override fun getJSMainModuleName(): String = "index"

        override fun getUseDeveloperSupport(): Boolean = BuildConfig.DEBUG

        override val isNewArchEnabled: Boolean = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED
        override val isHermesEnabled: Boolean = BuildConfig.IS_HERMES_ENABLED
      }

  override val reactHost: ReactHost
    get() = getDefaultReactHost(applicationContext, reactNativeHost)

  override fun onCreate() {
    super.onCreate()
    SoLoader.init(this, false)
    if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
      // If you opted-in for the New Architecture, we load the native entry point for this app.
      load()
    }
  }
}
