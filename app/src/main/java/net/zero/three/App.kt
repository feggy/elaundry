package net.zero.three

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import timber.log.Timber

class App : MultiDexApplication(), LifecycleObserver {

    companion object {
        private var mContext: App? = null
        val context: App by lazy {
            App.Companion.mContext
                ?: App.Companion.instance
        }
        val instance: App by lazy {
            App()
        }
    }

    override fun onCreate() {
        super.onCreate()
        App.Companion.mContext = this
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        ProcessLifecycleOwner.get().lifecycle.addObserver(this@App)
    }
}