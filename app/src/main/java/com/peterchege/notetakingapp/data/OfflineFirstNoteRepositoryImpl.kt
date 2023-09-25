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

import com.peterchege.notetakingapp.core.api.NetworkResult
import com.peterchege.notetakingapp.core.api.requests.NoteBody
import com.peterchege.notetakingapp.core.api.responses.Note
import com.peterchege.notetakingapp.core.util.DispatcherProvider
import com.peterchege.notetakingapp.core.work.sync_notes.SyncNotesWorkManager
import com.peterchege.notetakingapp.data.local.LocalNoteRepository
import com.peterchege.notetakingapp.data.remote.RemoteNoteRepository
import com.peterchege.notetakingapp.domain.repository.OfflineFirstNoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID

class OfflineFirstNoteRepositoryImpl(
    val localNoteRepository: LocalNoteRepository,
    val remoteNoteRepository: RemoteNoteRepository,
    val dispatcherProvider: DispatcherProvider,
    val syncNotesWorkManager: SyncNotesWorkManager,
) : OfflineFirstNoteRepository {

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

    override suspend fun addNote(noteBody: NoteBody) {
        withContext(dispatcherProvider.io) {
            try {
                val timestamp = Instant.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                val createdAt = timestamp.atZone(ZoneId.of("UTC+3")).format(formatter)
                val note = Note(
                    noteTitle = noteBody.noteTitle,
                    noteAuthorId = noteBody.noteAuthorId,
                    noteColor = noteBody.noteColor,
                    noteContent = noteBody.noteContent,
                    noteId = UUID.randomUUID().toString(),
                    createdAt = createdAt,
                    updatedAt = createdAt,
                )
                if (noteBody.noteAuthorId == "") {

                    localNoteRepository.addNote(note = note)
                } else {
                    val response = remoteNoteRepository.saveNoteRemote(noteBody = noteBody)
                    when (response) {
                        is NetworkResult.Success -> {
                            if (response.data.success && response.data.note != null) {
                                localNoteRepository.addNote(note = response.data.note)
                            } else {
                                localNoteRepository.addNote(note = note)
                            }
                        }
                        is NetworkResult.Error -> {
                            localNoteRepository.addNote(note = note)
                        }
                        else -> {  }
                    }
                    syncNotesWorkManager.startSyncingNotes(noteAuthorId = note.noteAuthorId)
                }

            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    override suspend fun deleteNoteById(noteId: String) {
        withContext(dispatcherProvider.io) {
            localNoteRepository.deleteLocalNoteById(noteId = noteId)
        }
    }

}