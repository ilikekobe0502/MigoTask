package com.migo.task.di

import android.content.Context
import androidx.room.Room
import com.migo.task.model.db.PassDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object DBModule {

    @Singleton
    @Provides
    fun provideContactsDatabase(@ApplicationContext appContext: Context): PassDatabase {
        return Room.databaseBuilder(
                appContext,
                PassDatabase::class.java,
                PassDatabase::class.java.simpleName
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideContactsDao(db: PassDatabase) = db.PassDao()
}

