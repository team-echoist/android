package com.echoist.linkedout.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.echoist.linkedout.data.repository.TokenRepository
import com.echoist.linkedout.data.repository.UserDataRepository
import com.echoist.linkedout.data.room.EssayStorageDB
import com.echoist.linkedout.data.room.EssayStoreDao
import com.echoist.linkedout.presentation.util.PREFS_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideUserDataRepository(sharedPreferences: SharedPreferences): UserDataRepository {
        return UserDataRepository(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideTokenRepository(): TokenRepository {
        return TokenRepository()
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): EssayStorageDB {
        return Room.databaseBuilder(
            context,
            EssayStorageDB::class.java,
            "todo-database"
        ).build()
    }

    @Provides
    fun provideEssayStoreDao(database: EssayStorageDB): EssayStoreDao {
        return database.essayStoreDao()
    }
}