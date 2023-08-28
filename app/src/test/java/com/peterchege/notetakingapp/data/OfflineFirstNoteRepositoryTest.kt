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

import app.cash.turbine.test
import com.peterchege.notetakingapp.MainDispatcherRule
import com.peterchege.notetakingapp.TestDispatcherProvider
import com.peterchege.notetakingapp.core.work.sync_notes.SyncNotesWorkManager
import com.peterchege.notetakingapp.data.local.LocalNoteRepository
import com.peterchege.notetakingapp.data.remote.RemoteNoteRepository
import com.peterchege.notetakingapp.domain.models.Note
import com.peterchege.notetakingapp.domain.models.RemoteDataResult
import com.peterchege.notetakingapp.domain.repository.OfflineFirstNoteRepository
import com.peterchege.notetakingapp.fake.note1
import com.peterchege.notetakingapp.fake.note2
import com.peterchege.notetakingapp.fake.note3
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test



class OfflineFirstNoteRepositoryTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    lateinit var repo: OfflineFirstNoteRepository

    val mockLocalNotesRepository = mockk<LocalNoteRepository>()
    val remoteMockNotesRepo = mockk<RemoteNoteRepository>()
    val syncNotesWorkManager = mockk<SyncNotesWorkManager>()

    @Before
    fun setUp() {
        mainDispatcherRule.testDispatcher
        repo = OfflineFirstNoteRepositoryImpl(
            localNoteRepository = mockLocalNotesRepository,
            remoteNoteRepository = remoteMockNotesRepo,
            dispatcherProvider = TestDispatcherProvider(),
            syncNotesWorkManager = syncNotesWorkManager
        )
    }

    @Test
    fun `Verify the notes are fetched from the local cache`() = runTest {
        every { mockLocalNotesRepository.getLocalNotes() } returns flowOf(listOf(note1, note2, note3))
        repo.getAllNotes().test {
            assert(awaitItem().contains(note1))
            awaitComplete()
        }
        verify (atLeast = 1){
            mockLocalNotesRepository.getLocalNotes()
        }
    }

    @Test
    fun `Given a network error, the note will be saved in the local cache `() =
        runTest {
            coEvery { remoteMockNotesRepo.saveNoteRemote(note = any()) } returns
                    RemoteDataResult.Error(message = "Error")
            coEvery { mockLocalNotesRepository.addNote(any()) } just runs

            repo.addNote(note = note1)

            coVerify {
                mockLocalNotesRepository.addNote(any())
            }
        }

    @Test
    fun `Given a network success, the note will be saved in the local cache `() = runTest {
        coEvery { remoteMockNotesRepo.saveNoteRemote(note = any()) } returns
                RemoteDataResult.Success(data = note1)
        coEvery { mockLocalNotesRepository.addNote(any()) } just runs
        coEvery { syncNotesWorkManager.startSyncingNotes(any()) } just runs

        repo.addNote(note = note1)

        coVerify {
            mockLocalNotesRepository.addNote(any())
            syncNotesWorkManager.startSyncingNotes(any())
        }
    }

}