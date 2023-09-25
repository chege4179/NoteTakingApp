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
package com.peterchege.notetakingapp.core.work.sync_notes

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.peterchege.notetakingapp.R
import com.peterchege.notetakingapp.core.util.Constants
import com.peterchege.notetakingapp.data.local.LocalNoteRepository
import com.peterchege.notetakingapp.data.remote.RemoteNoteRepository
import com.peterchege.notetakingapp.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.withContext
import kotlin.random.Random

class SyncNotesWorker(
    val appContext: Context,
    val workerParameters: WorkerParameters,
    val localNoteRepository: LocalNoteRepository,
    val remoteNoteRepository: RemoteNoteRepository,
    val authRepository: AuthRepository,

    ) : CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO){
            val outOfSyncNotes = localNoteRepository.getNotesBySyncStatus(isInSync = false)
            if (outOfSyncNotes.isNotEmpty()) {
                try {
//                    startForegroundService(notificationInfo = "Trying to sync your notes to the cloud.....")
//                    val authorId = inputData.getString("noteAuthorId") ?: return@withContext Result.success()
//                    if (authorId == ""){
//                        return@withContext Result.success()
//                    }
//                    localNoteRepository.updateNoteAuthorId(noteAuthorId = authorId)
//                    remoteNoteRepository.deleteAllNotes(authorId = authorId)
//                    val localNotes = localNoteRepository.getLocalNotes().first()
//                    localNotes.forEach { note ->
//                        remoteNoteRepository.saveNoteRemote(note = note)
//                    }
//                    localNoteRepository.updateNoteSyncStatus(syncStatus = true)
                    startForegroundService(notificationInfo = "Sync successful")
                    return@withContext Result.success()
                } catch (e: Throwable) {
                    println("Error occurred In the worker : ${e.localizedMessage ?: e.message}")
                    startForegroundService(notificationInfo = "Sync failed !!")
                    return@withContext Result.failure()
                }

            } else {
                startForegroundService(notificationInfo = "No need for sync.....")
                return@withContext Result.success()
            }
        }


    }


    private suspend fun startForegroundService(notificationInfo: String) {
        setForeground(
            ForegroundInfo(
                Random.nextInt(),
                NotificationCompat.Builder(appContext, Constants.NOTIFICATION_CHANNEL)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle(notificationInfo)
                    .build()

            )
        )

    }
}

class SyncNotesWorkerFactory(
    val localNoteRepository: LocalNoteRepository,
    val remoteNoteRepository: RemoteNoteRepository,
    val authRepository: AuthRepository,
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            SyncNotesWorker::class.java.name ->
                SyncNotesWorker(
                    appContext = appContext,
                    workerParameters = workerParameters,
                    localNoteRepository = localNoteRepository,
                    remoteNoteRepository = remoteNoteRepository,
                    authRepository = authRepository,
                )

            else -> null
        }
    }
}
