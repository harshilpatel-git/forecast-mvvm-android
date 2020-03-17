package com.harshil.weatherforecastmvvm.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson


class StringListConverter {
    @TypeConverter
    fun fromString(stringListString: String): List<String> {
        return stringListString.split(",").map { it }
    }

    @TypeConverter
    fun toString(stringList: List<String>): String {
        return stringList.joinToString(separator = ",")
    }
}