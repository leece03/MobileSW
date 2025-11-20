package com.example.re0

import androidx.room.TypeConverter
import com.example.re0.model.Achievement
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun fromAchievementList(value: List<Achievement>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toAchievementList(value: String): List<Achievement> {
        val listType = object : TypeToken<List<Achievement>>() {}.type
        return Gson().fromJson(value, listType)
    }
}