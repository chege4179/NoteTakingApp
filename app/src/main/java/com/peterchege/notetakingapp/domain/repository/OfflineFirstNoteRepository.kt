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
package com.peterchege.notetakingapp.domain.repository

import com.peterchege.notetakingapp.domain.models.Note
import kotlinx.coroutines.flow.Flow

interface OfflineFirstNoteRepository {


    fun getAllNotes(): Flow<List<Note>>


    fun getNoteById(noteId: String):Flow<Note?>

    fun searchNotes(query:String):Flow<List<Note>>


    suspend fun addNote(note:Note)

    suspend fun deleteNoteById(noteId:String)

    suspend fun syncNote(noteId:String)

    suspend fun updateSyncStatus(syncStatus:Boolean)
}