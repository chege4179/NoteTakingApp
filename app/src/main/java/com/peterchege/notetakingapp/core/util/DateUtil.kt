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
package com.peterchege.notetakingapp.core.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
fun generateFormatDate(date: LocalDate):String{
    var dateCount:String;
    var monthCount:String;
    if (date.dayOfMonth < 10){
        dateCount = "0${date.dayOfMonth}"
    }else{
        dateCount = date.dayOfMonth.toString()
    }
    if (date.monthValue < 10){
        monthCount ="0${date.monthValue}"
    }else{
        monthCount = date.monthValue.toString()
    }
    return "${dateCount}/${monthCount}/${date.year}"

}