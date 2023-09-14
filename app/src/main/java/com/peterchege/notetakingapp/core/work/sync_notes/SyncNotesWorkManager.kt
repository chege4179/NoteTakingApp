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
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.peterchege.notetakingapp.core.work.WorkConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map


interface SyncNotesWorkManager {
    val isSyncing: Flow<Boolean>

    fun startSyncingNotes(noteAuthorId: String)


}

class SyncNotesWorkManagerImpl(
    val context: Context,
) : SyncNotesWorkManager {

    override val isSyncing: Flow<Boolean> =
        WorkManager.getInstance(context)
            .getWorkInfosForUniqueWorkFlow(WorkConstants.syncNotesWorkerName)
            .map{ workInfos -> workInfos.any { workInfo -> workInfo.state == WorkInfo.State.RUNNING } }
            .conflate()


    override fun startSyncingNotes(noteAuthorId: String) {
        val inputData = workDataOf("noteAuthorId" to noteAuthorId)
        val syncNotesRequest = OneTimeWorkRequestBuilder<SyncNotesWorker>()
            .setInputData(inputData)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(
                        NetworkType.CONNECTED
                    )
                    .build()
            )
            .build()
        val workManager = WorkManager.getInstance(context)
        workManager.beginUniqueWork(
            WorkConstants.syncNotesWorkerName,
            ExistingWorkPolicy.KEEP,
            syncNotesRequest
        )
            .enqueue()
    }

}