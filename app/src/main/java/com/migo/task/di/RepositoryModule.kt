package com.migo.task.di

import com.migo.task.model.api.ApiService
import com.migo.task.model.repository.RoloDbRepository
import com.migo.task.model.repository.RoloRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Provides
    fun provideRoloRepository(apiService: ApiService, dbRepository: RoloDbRepository): RoloRepository {
        return RoloRepository(apiService, dbRepository)
    }
}