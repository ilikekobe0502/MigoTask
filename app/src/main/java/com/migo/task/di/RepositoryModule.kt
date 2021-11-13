package com.migo.task.di

import com.migo.task.model.api.ApiService
import com.migo.task.model.repository.MigoDbRepository
import com.migo.task.model.repository.MigoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Provides
    fun provideRoloRepository(apiService: ApiService, dbRepository: MigoDbRepository): MigoRepository {
        return MigoRepository(apiService, dbRepository)
    }
}