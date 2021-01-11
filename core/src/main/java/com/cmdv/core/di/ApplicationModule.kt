package com.cmdv.core.di

import com.cmdv.core.managers.ZipManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
class ApplicationModule {

    @Provides
    fun provideZipManager(): ZipManager = ZipManager()

}