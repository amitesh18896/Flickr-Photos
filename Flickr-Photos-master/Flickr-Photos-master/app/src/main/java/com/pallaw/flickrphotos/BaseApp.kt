package com.pallaw.flickrphotos

import android.app.Application
import androidx.annotation.NonNull
import timber.log.Timber

/**
 * Created by Pallaw Pathak on 22/03/20. - https://www.linkedin.com/in/pallaw-pathak-a6a324a1/
 */
class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()

        //setup timber
        setupTimber()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun log(
                    priority: Int,
                    tag: String?,
                    @NonNull msg: String,
                    t: Throwable?
                ) {
                    super.log(priority, "flickr_logs_$tag", msg, t)
                }
            })
        }
    }
}