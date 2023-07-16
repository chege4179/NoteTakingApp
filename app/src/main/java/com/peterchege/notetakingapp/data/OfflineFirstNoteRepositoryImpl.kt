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
package com.peterchege.notetakingapp.data

import com.peterchege.notetakingapp.core.util.DispatcherProvider
import com.peterchege.notetakingapp.core.work.sync_notes.SyncNotesWorkManager
import com.peterchege.notetakingapp.data.local.LocalNoteRepository
import com.peterchege.notetakingapp.data.remote.RemoteNoteRepository
import com.peterchege.notetakingapp.domain.models.Note
import com.peterchege.notetakingapp.domain.models.RemoteDataResult
import com.peterchege.notetakingapp.domain.repository.OfflineFirstNoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.withContext
import timber.log.Timber

class OfflineFirstNoteRepositoryImpl (
    val localNoteRepository: LocalNoteRepository,
    val remoteNoteRepository: RemoteNoteRepository,
    val dispatcherProvider: DispatcherProvider,
    val syncNotesWorkManager: SyncNotesWorkManager,
):OfflineFirstNoteRepository {

    override fun getAllNotes(): Flow<List<Note>> {
        return localNoteRepository.getLocalNotes().flowOn(dispatcherProvider.io)
    }

    override suspend fun addNote(note: Note) {
        withContext(dispatcherProvider.io){
            try {
                val response = remoteNoteRepository.saveNoteRemote(note = note)
                when(response){
                    is RemoteDataResult.Success -> {
                        localNoteRepository.addNote(note = response.data)
                    }
                    is RemoteDataResult.Error -> {
                        localNoteRepository.addNote(note = note.copy(isInSync = false))
                    }
                }
                syncNotesWorkManager.startSyncingNotes("")
            }catch (e:Exception){
                Timber.e(e)
                localNoteRepository.addNote(note = note.copy(isInSync = false))
            }


        }
    }

    override suspend fun deleteNoteById(noteId: String) {
        withContext(dispatcherProvider.io){
            localNoteRepository.deleteLocalNoteById(noteId = noteId)
        }
    }

}