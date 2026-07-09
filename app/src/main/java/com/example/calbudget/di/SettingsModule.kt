package com.example.calbudget.di

import com.example.calbudget.data.repository.SettingsRepository
import com.example.calbudget.data.repository.SettingsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {

    // Bind = "kalau ada yang minta SettingsRepository,
    //         berikan SettingsRepositoryImpl"
    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepository
}