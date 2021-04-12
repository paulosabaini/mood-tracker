package org.sabaini.moodtracker.di

import android.content.Context
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.sabaini.moodtracker.db.MoodTrackerDao
import org.sabaini.moodtracker.db.MoodTrackerDb
import org.sabaini.moodtracker.repository.MoodTrackerRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoodTrackerDatabase(@ApplicationContext context: Context): MoodTrackerDb {
        return Room.databaseBuilder(
            context.applicationContext,
            MoodTrackerDb::class.java,
            "moods"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideMoodTrackerDao(database: MoodTrackerDb): MoodTrackerDao {
        return database.moodTrackerDao()
    }

    @Provides
    @Singleton
    fun provideMoodTrackerRepository(moodTrackerDao: MoodTrackerDao): MoodTrackerRepository {
        return MoodTrackerRepository(moodTrackerDao)
    }

    @Provides
    @Singleton
    fun provideBundledEmojiCompatConfig(@ApplicationContext context: Context): BundledEmojiCompatConfig {
        return BundledEmojiCompatConfig(context)
    }
}