package com.adorah.ExploroTravel.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.adorah.ExploroTravel.data.repository.ExploroRepository
import com.adorah.ExploroTravel.data.repository.ExploroRepositoryImpl
import com.example.todo.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // migration functionality
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
//            super.migrate(db)
            db.execSQL(
                "ALTER TABLE todos ADD COLUMN firebase_id " +
                        "INTEGER NOT NULL DEFAULT 0"
            )
        }
    }

    @Provides
    @Singleton
    fun provideExploroRepository(): ExploroRepository {
        return ExploroRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideExploroDatabase(@ApplicationContext context: Context):
            AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "Exploro_db"
        ).addMigrations(MIGRATION_1_2).build()
    }
}
















