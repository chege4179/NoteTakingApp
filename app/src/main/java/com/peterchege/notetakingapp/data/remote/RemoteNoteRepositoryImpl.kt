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
package com.peterchege.notetakingapp.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.peterchege.notetakingapp.core.util.DispatcherProvider
import com.peterchege.notetakingapp.domain.mappers.noteToNoteMap
import com.peterchege.notetakingapp.domain.models.Note
import com.peterchege.notetakingapp.domain.models.RemoteDataResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

class RemoteNoteRepositoryImpl(
    val fireStore: FirebaseFirestore,
    val defaultDispatcherProvider: DispatcherProvider,
) : RemoteNoteRepository {


    override suspend fun getAllRemoteNotes(authorId: String): RemoteDataResult<List<Note>> {
        return withContext(defaultDispatcherProvider.io) {
            val collectionRef = fireStore
                .collection("notes")
                .whereEqualTo("noteAuthorId", authorId)

            val querySnapshot = collectionRef.get().await()
            val notes = mutableListOf<Note>()

            for (document in querySnapshot.documents) {
                val data = document.data
                val noteId = data?.get("noteId") as? String ?: ""
                val noteTitle = data?.get("noteTitle") as? String ?: ""
                val noteContent = data?.get("noteContent") as? String ?: ""
                val noteColor = data?.get("noteColor") as? Int ?: 0
                val noteAuthorId = data?.get("noteAuthorId") as? String ?: ""
                val noteCreatedAt = data?.get("noteCreatedAt") as? String ?: ""
                val noteCreatedOn = data?.get("noteCreatedOn") as? String ?: ""
                val isInSync = data?.get("isInSync") as? Boolean ?: false
                val isDeleted = data?.get("isDeleted") as? Boolean ?: false
                val note = Note(
                    noteId = noteId,
                    noteTitle = noteTitle,
                    noteContent = noteContent,
                    noteColor = noteColor,
                    noteAuthorId = noteAuthorId,
                    noteCreatedAt = noteCreatedAt,
                    noteCreatedOn = noteCreatedOn,
                    isInSync = isInSync,
                    isDeleted = isDeleted,
                )
                notes.add(note)
            }
            return@withContext RemoteDataResult.Success(data = notes)

        }

    }

    override suspend fun deleteAllNotes(authorId: String): RemoteDataResult<String> {
        val response = getAllRemoteNotes(authorId = authorId)
        when (response) {
            is RemoteDataResult.Error -> {
                return RemoteDataResult.Error(message = "An unexpected error occurred deleting notes")
            }

            is RemoteDataResult.Success -> {
                response.data.forEach {
                    deleteNoteById(noteId = it.noteId)
                }
                return RemoteDataResult.Success(data = "All notes deleted successfully")
            }
        }


    }

    override suspend fun deleteNoteById(noteId: String): RemoteDataResult<String> =
        withContext(defaultDispatcherProvider.io) {
            try {
                val docRef = fireStore.collection("notes").document(noteId)
                docRef.update("isDeleted", true).await()
                return@withContext RemoteDataResult.Success(data = "Note Deleted successfully")
            } catch (e: Exception) {
                Timber.e(e)
                return@withContext RemoteDataResult.Error(message = "An unexpected error occurred")
            }

        }


    override suspend fun saveNoteRemote(note: Note): RemoteDataResult<Note> {
        return withContext(defaultDispatcherProvider.io) {
            try {
                val docRef = fireStore.collection("notes").document()
                docRef.set(note.noteToNoteMap()).await()
                return@withContext RemoteDataResult.Success(data = note)


            } catch (e: Exception) {
                Timber.e("Supabase Error", "Error: " + e.message)
                return@withContext RemoteDataResult.Error(
                    message = e.message
                        ?: "An error occurred saving notes"

                )
            }
        }
    }
}


//            collectionRef.addSnapshotListener { snapshot, error ->
//                if (error != null) {
//                    return@addSnapshotListener (
//                            RemoteDataResult.Error(
//                                message = error.message ?: "An unexpected error occurred"
//                            )
//                            )
//
//                }
//                val snapshotData = snapshot?.documents ?: emptyList()
//
//                val allNotes = snapshotData.map {
//                    val data = it.data
//                    val noteId = data?.get("noteId") as? String ?: ""
//                    val noteTitle = data?.get("noteTitle") as? String ?: ""
//                    val noteContent = data?.get("noteContent") as? String ?: ""
//                    val noteColor = data?.get("noteColor") as? Int ?: 0
//                    val noteAuthorId = data?.get("noteAuthorId") as? String ?: ""
//                    val noteCreatedAt = data?.get("noteCreatedAt") as? String ?: ""
//                    val noteCreatedOn = data?.get("noteCreatedOn") as? String ?: ""
//                    val isInSync = data?.get("isInSync") as? Boolean ?: false
//                    val isDeleted = data?.get("isDeleted") as? Boolean ?: false
//                    return@map Note(
//                        noteId = noteId,
//                        noteTitle = noteTitle,
//                        noteContent = noteContent,
//                        noteColor = noteColor,
//                        noteAuthorId = noteAuthorId,
//                        noteCreatedAt = noteCreatedAt,
//                        noteCreatedOn = noteCreatedOn,
//                        isInSync = isInSync,
//                        isDeleted = isDeleted,
//                    )
//                }
//
//                return@addSnapshotListener (RemoteDataResult.Success(data = allNotes))


