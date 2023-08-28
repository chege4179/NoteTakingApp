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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import timber.log.Timber

class OfflineFirstNoteRepositoryImpl (
    val localNoteRepository: LocalNoteRepository,
    val remoteNoteRepository: RemoteNoteRepository,
    val dispatcherProvider: DispatcherProvider,
    val syncNotesWorkManager: SyncNotesWorkManager,
):OfflineFirstNoteRepository {

    override fun getAllNotes(): Flow<List<Note>> {
        return localNoteRepository.getLocalNotes()
            .onStart { emptyList<Note>() }
            .catch { emptyList<Note>() }
            .flowOn(dispatcherProvider.io)
    }

    override fun getNoteById(noteId: String): Flow<Note?> {
        return localNoteRepository.getLocalNoteById(noteId = noteId)
            .catch { emit(null) }

    }

    override fun searchNotes(query: String): Flow<List<Note>> {
        return localNoteRepository.searchNotes(query = query)
    }

    override suspend fun updateSyncStatus(syncStatus: Boolean) {
        return localNoteRepository.updateNoteSyncStatus(syncStatus)
    }

    override suspend fun addNote(note: Note) {
        withContext(dispatcherProvider.io){
            try {
                if (note.noteAuthorId == ""){
                    localNoteRepository.addNote(note = note)
                }else{
                    val response = remoteNoteRepository.saveNoteRemote(note = note)
                    when(response){
                        is RemoteDataResult.Success -> {
                            localNoteRepository.addNote(note = note)
                        }
                        is RemoteDataResult.Error -> {
                            localNoteRepository.addNote(note = note.copy(isInSync = false))
                        }
                    }
                    syncNotesWorkManager.startSyncingNotes(noteAuthorId = note.noteAuthorId)
                }

            }catch (e:Exception){
                Timber.e(e)
                localNoteRepository.addNote(note = note.copy(isInSync = false))
            }


        }
    }

    override suspend fun syncNote(noteId: String) {
        val note = localNoteRepository.getLocalNoteById(noteId).first() ?: return
        val result = remoteNoteRepository.getRemoteNoteById(noteId)
        when(result){
            is RemoteDataResult.Error -> {
                // Note not found in the firestore so we update it
                remoteNoteRepository.saveNoteRemote(note = note)
            }
            is RemoteDataResult.Success -> {

            }
        }
    }

    override suspend fun deleteNoteById(noteId: String) {
        withContext(dispatcherProvider.io){
            localNoteRepository.deleteLocalNoteById(noteId = noteId)
        }
    }

}