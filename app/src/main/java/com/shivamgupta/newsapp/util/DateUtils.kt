package com.shivamgupta.newsapp.util

import java.text.SimpleDateFormat
import java.util.Locale

object DateUtils {
    fun getFormattedDate(
        currentFormat: String,
        desiredFormat: String,
        dateText: String
    ): String{
        return try {
            val inputFormat = SimpleDateFormat(currentFormat, Locale.getDefault())
            val outputFormat = SimpleDateFormat(desiredFormat, Locale.getDefault())
            val date = inputFormat.parse(dateText)

            if (date != null) {
                outputFormat.format(date)
            } else {
                ""
            }
        } catch (e: Exception){
            //TODO("Log exception")
            ""
        }
    }
}