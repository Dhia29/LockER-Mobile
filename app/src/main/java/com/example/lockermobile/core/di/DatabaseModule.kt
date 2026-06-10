package com.example.lockermobile.core.di

import android.content.Context
import androidx.room.Room
import com.example.lockermobile.data.local.LockERDatabase
import com.example.lockermobile.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LockERDatabase {
        return Room.databaseBuilder(
            context,
            LockERDatabase::class.java,
            "locker_db"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    fun provideUserDao(db: LockERDatabase): UserDao = db.userDao

    @Provides
    fun provideJobDao(db: LockERDatabase): JobDao = db.jobDao

    @Provides
    fun providePostDao(db: LockERDatabase): PostDao = db.postDao

    @Provides
    fun provideApplicationDao(db: LockERDatabase): ApplicationDao = db.applicationDao

    @Provides
    fun provideNotificationDao(db: LockERDatabase): NotificationDao = db.notificationDao

    @Provides
    fun provideCompanyDao(db: LockERDatabase): CompanyDao = db.companyDao
}
