package com.bendeng.swingproject.di

import com.bendeng.domain.repository.UnplashRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import repository.UnplashRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindUnplashRepository(unplashRepositoryImpl: UnplashRepositoryImpl): UnplashRepository
}