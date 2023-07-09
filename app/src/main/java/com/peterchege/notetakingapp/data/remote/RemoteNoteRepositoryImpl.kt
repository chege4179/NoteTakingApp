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

import com.peterchege.notetakingapp.core.util.Constants
import com.peterchege.notetakingapp.core.util.DefaultDispatcherProvider
import com.peterchege.notetakingapp.core.util.DispatcherProvider
import com.peterchege.notetakingapp.domain.mappers.noteMapToNote
import com.peterchege.notetakingapp.domain.mappers.noteToNoteMap
import com.peterchege.notetakingapp.domain.models.Note
import com.peterchege.notetakingapp.domain.models.RemoteDataResult
import io.appwrite.ID
import io.appwrite.Query
import io.appwrite.services.Databases
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import timber.log.Timber

class RemoteNoteRepositoryImpl(
    val appWriteDatabase: Databases,
    val defaultDispatcherProvider: DispatcherProvider,
) : RemoteNoteRepository {
    override suspend fun getAllRemoteNotes(authorId: String): RemoteDataResult<List<Note>> {
        return withContext(defaultDispatcherProvider.io){
            try {
                val response = appWriteDatabase.listDocuments(
                    databaseId = Constants.DATABASE_ID,
                    collectionId = Constants.COLLECTION_ID,
                    queries = listOf(
                        Query.equal(attribute = "noteAuthorId",value = authorId),
                    )
                )
                val notes = response.documents.map {
                    it.toMap().noteMapToNote()
                }
                return@withContext RemoteDataResult.Success(data = notes)
            }catch (e:Exception){
                return@withContext  RemoteDataResult.Error(message = e.message
                    ?: "An unexpected exception occurred")
            }

        }

    }

    override suspend fun deleteAllNotes(authorId: String): RemoteDataResult<String> {
        val response = getAllRemoteNotes(authorId = authorId)
        when(response){
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

    override suspend fun deleteNoteById(noteId: String): RemoteDataResult<String> {
        return withContext(defaultDispatcherProvider.io){
            try {
                appWriteDatabase.deleteDocument(
                    databaseId = Constants.DATABASE_ID,
                    collectionId = Constants.COLLECTION_ID,
                    documentId = noteId
                )
                return@withContext RemoteDataResult.Success(data = "Note deleted successfully")
            }catch (e:Exception){
                Timber.e(e)
                return@withContext RemoteDataResult.Error(message = "An unexpected error occurred")
            }

        }
    }

    override suspend fun saveNoteRemote(note: Note): RemoteDataResult<Note> {
        return withContext(defaultDispatcherProvider.io){
            try {
                appWriteDatabase.createDocument(
                    databaseId = Constants.DATABASE_ID,
                    collectionId = Constants.COLLECTION_ID,
                    documentId = note.noteId,
                    data = note.noteToNoteMap(),
                )
                return@withContext RemoteDataResult.Success(data = note)
            } catch (e: Exception) {
                Timber.e("Appwrite", "Error: " + e.message)
                return@withContext  RemoteDataResult.Error(message = e.message
                    ?:"An error occurred saving notes")
            }
        }
    }


}