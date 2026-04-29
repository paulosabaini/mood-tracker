package org.sabaini.moodtracker.di

import android.content.Context
import androidx.emoji2.bundled.BundledEmojiCompatConfig
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.sabaini.moodtracker.data.local.dao.MoodDao
import org.sabaini.moodtracker.data.local.db.MoodTrackerDatabase
import org.sabaini.moodtracker.data.local.prefs.PreferenceManager
import org.sabaini.moodtracker.data.repository.MoodTrackerRepositoryImpl
import org.sabaini.moodtracker.domain.repository.MoodTrackerRepository
import org.sabaini.moodtracker.domain.usecase.GetMoodsUseCase
import org.sabaini.moodtracker.domain.usecase.GetStatisticsUseCase
import org.sabaini.moodtracker.domain.usecase.SaveMoodUseCase
import javax.inject.Singleton

private const val MOOD_TRACKER_DATABASE_NAME = "mood_tracker_db"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoodTrackerDatabase(@ApplicationContext context: Context): MoodTrackerDatabase {
        return Room.databaseBuilder(
            context,
            MoodTrackerDatabase::class.java,
            MOOD_TRACKER_DATABASE_NAME,
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    fun provideMoodTrackerDao(database: MoodTrackerDatabase): MoodDao {
        return database.moodDao()
    }

    @Provides
    @Singleton
    fun provideMoodTrackerRepository(moodDao: MoodDao): MoodTrackerRepository {
        return MoodTrackerRepositoryImpl(moodDao)
    }

    @Provides
    @Singleton
    fun provideBundledEmojiCompatConfig(@ApplicationContext context: Context): BundledEmojiCompatConfig {
        return BundledEmojiCompatConfig(context)
    }

    @Provides
    @Singleton
    fun provideGetMoodsUseCase(repository: MoodTrackerRepository): GetMoodsUseCase {
        return GetMoodsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSaveMoodUseCase(repository: MoodTrackerRepository): SaveMoodUseCase {
        return SaveMoodUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetStatisticsUseCase(repository: MoodTrackerRepository): GetStatisticsUseCase {
        return GetStatisticsUseCase(repository)
    }

    @Provides
    @Singleton
    fun providePreferenceManager(@ApplicationContext context: Context): PreferenceManager {
        return PreferenceManager(context)
    }
}
