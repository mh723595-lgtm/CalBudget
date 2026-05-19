package com.example.calbudget.di

import android.content.Context
import androidx.room.Room
import com.example.calbudget.data.local.database.FinanceDatabase
import com.example.calbudget.data.local.dao.TransactionDao
import com.example.calbudget.data.repository.TransactionRepository
import com.example.calbudget.data.repository.TransactionRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)  // Hidup selama app hidup
object DatabaseModule {

    // Provide Room Database — singleton
    @Provides
    @Singleton
    fun provideFinanceDatabase(
        @ApplicationContext context: Context
    ): FinanceDatabase {
        return Room.databaseBuilder(
            context,
            FinanceDatabase::class.java,
            FinanceDatabase.DATABASE_NAME
        )
            // Untuk development: kalau schema berubah, hapus & buat ulang DB
            // JANGAN pakai ini di production — pakai Migration!
            .fallbackToDestructiveMigration()
            .build()
    }

    // Provide DAO dari Database
    @Provides
    @Singleton
    fun provideTransactionDao(database: FinanceDatabase): TransactionDao {
        return database.transactionDao()
    }
}

// Module terpisah untuk binding interface → implementasi
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // Bind = "kalau ada yang minta TransactionRepository,
    //         berikan TransactionRepositoryImpl"
    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        impl: TransactionRepositoryImpl
    ): TransactionRepository
}