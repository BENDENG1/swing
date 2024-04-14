package com.bendeng.swingproject.di

import com.bendeng.data.remote.UnplashApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideUnplashService(retrofit: Retrofit): UnplashApi =
        retrofit.create(UnplashApi::class.java)
}