package com.example.lockermobile.data.local.util

import androidx.room.TypeConverter
import com.example.lockermobile.domain.model.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromUserRole(role: UserRole): String = role.name

    @TypeConverter
    fun toUserRole(role: String): UserRole = try {
        UserRole.valueOf(role)
    } catch (e: IllegalArgumentException) {
        UserRole.JOB_SEEKER
    }

    @TypeConverter
    fun fromStringList(value: List<String>): String = Json.encodeToString(value)

    @TypeConverter
    fun toStringList(value: String): List<String> = try {
        Json.decodeFromString<List<String>>(value)
    } catch (e: Exception) {
        emptyList()
    }

    @TypeConverter
    fun fromExperienceList(value: List<Experience>): String = Json.encodeToString(value)

    @TypeConverter
    fun toExperienceList(value: String): List<Experience> = try {
        Json.decodeFromString<List<Experience>>(value)
    } catch (e: Exception) {
        emptyList()
    }

    @TypeConverter
    fun fromEducationList(value: List<Education>): String = Json.encodeToString(value)

    @TypeConverter
    fun toEducationList(value: String): List<Education> = try {
        Json.decodeFromString<List<Education>>(value)
    } catch (e: Exception) {
        emptyList()
    }
}
