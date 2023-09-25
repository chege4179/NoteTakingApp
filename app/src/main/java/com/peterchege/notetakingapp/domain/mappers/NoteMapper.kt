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
package com.peterchege.notetakingapp.domain.mappers

import com.peterchege.notetakingapp.core.api.responses.Note
import com.peterchege.notetakingapp.core.room.entites.NoteEntity

fun List<NoteEntity>.toExternalListModel():List<Note>{
    return this.map { it.toExternalModel() }
}

fun NoteEntity.toExternalModel():Note {
    return Note(
        noteId = noteId,
        noteTitle =noteTitle,
        noteContent = noteContent,
        noteColor = noteColor,
        noteAuthorId = noteAuthorId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Note.toEntity(isInSync:Boolean):NoteEntity {
    return NoteEntity(
        noteId = noteId,
        noteTitle =noteTitle,
        noteContent = noteContent,
        noteColor = noteColor,
        noteAuthorId = noteAuthorId,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isInSync = isInSync,
    )
}