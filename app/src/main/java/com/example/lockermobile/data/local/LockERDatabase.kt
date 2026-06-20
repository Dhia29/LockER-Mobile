package com.example.lockermobile.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.lockermobile.data.local.dao.*
import com.example.lockermobile.data.local.entity.*
import com.example.lockermobile.data.local.util.Converters

@Database(
    entities = [
        UserEntity::class,
        JobEntity::class,
        CompanyEntity::class,
        PostEntity::class,
        NotificationEntity::class,
        ApplicationEntity::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class LockERDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val jobDao: JobDao
    abstract val postDao: PostDao
    abstract val applicationDao: ApplicationDao
    abstract val notificationDao: NotificationDao
    abstract val companyDao: CompanyDao
}
