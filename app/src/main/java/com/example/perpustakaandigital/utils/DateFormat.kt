@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.perpustakaandigital.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateFormat {

    @SuppressLint("SimpleDateFormat")
    private fun formatDate(date : String, format: String): String {
        var result = ""
        var timeFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        try{

            val timeDate = timeFormat.parse(date)
            val newFormat = SimpleDateFormat(format)

            result = newFormat.format(timeDate)

        } catch (e:ParseException){
            e.printStackTrace()
        }

        return result
    }

    fun getLongDate(date: String):String{
        return formatDate(date,"yyyy-MM-dd")
    }
}