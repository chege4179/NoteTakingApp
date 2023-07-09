/*
 * Copyright 2023 Note Taking App by Peter Chege
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.peterchege.notetakingapp.core.di

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import com.google.firebase.crashlytics.BuildConfig
import com.peterchege.notetakingapp.core.crashlytics.CrashlyticsTree
import com.peterchege.notetakingapp.core.util.Constants
import com.peterchege.notetakingapp.core.work.WorkConstants
import com.peterchege.notetakingapp.core.work.sync_notes.SyncNotesWorkerFactory
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import timber.log.Timber

class NoteTakingApp:Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()
        initTimber()
        startKoin {
            androidLogger()
            androidContext(this@NoteTakingApp)
            workManagerFactory()
            modules(dispatchersModule + databaseModule + firebaseModule + repositoryModule
                    + viewModelModule + workModule + datastoreModule)
        }
        setUpWorkerManagerNotificationChannel()

    }


    private fun initTimber() = when {
        BuildConfig.DEBUG -> {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return super.createStackElementTag(element) + ":" + element.lineNumber
                }
            })
        }
        else -> {
            Timber.plant(CrashlyticsTree())
        }
    }
    private fun setUpWorkerManagerNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL,
                WorkConstants.syncNotesWorkerName,
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

    }
}