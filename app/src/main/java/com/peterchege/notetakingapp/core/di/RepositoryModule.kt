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
package com.peterchege.notetakingapp.core.di

import com.peterchege.notetakingapp.data.AuthRepositoryImpl
import com.peterchege.notetakingapp.data.NetworkInfoRepositoryImpl
import com.peterchege.notetakingapp.data.OfflineFirstNoteRepositoryImpl
import com.peterchege.notetakingapp.data.local.LocalNoteRepository
import com.peterchege.notetakingapp.data.local.LocalNoteRepositoryImpl
import com.peterchege.notetakingapp.data.remote.RemoteNoteRepository
import com.peterchege.notetakingapp.data.remote.RemoteNoteRepositoryImpl
import com.peterchege.notetakingapp.domain.repository.AuthRepository
import com.peterchege.notetakingapp.domain.repository.NetworkInfoRepository
import com.peterchege.notetakingapp.domain.repository.OfflineFirstNoteRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    single<NetworkInfoRepository>{
        NetworkInfoRepositoryImpl(
            context = androidContext(),
            dispatcherProvider = get()
        )
    }
    single<AuthRepository> {
        AuthRepositoryImpl(
            auth = get()
        )
    }

    single<LocalNoteRepository> {
        LocalNoteRepositoryImpl(db = get())
    }

    single<RemoteNoteRepository> {
        RemoteNoteRepositoryImpl()
    }
    single<OfflineFirstNoteRepository> {
        OfflineFirstNoteRepositoryImpl(
            localNoteRepository = get(),
            remoteNoteRepository = get(),
            dispatcherProvider = get(),
        )
    }

}