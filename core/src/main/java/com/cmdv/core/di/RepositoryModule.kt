package com.cmdv.core.di

import com.cmdv.data.repositories.FileRepositoryImpl
import com.cmdv.domain.repositories.FilesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@InstallIn(ApplicationComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindFilesRepository(impl: FileRepositoryImpl): FilesRepository

}