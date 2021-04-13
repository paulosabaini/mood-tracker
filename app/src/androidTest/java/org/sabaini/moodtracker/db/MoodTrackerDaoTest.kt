package org.sabaini.moodtracker.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class MoodTrackerDaoTest {
    private lateinit var database: MoodTrackerDb
    private lateinit var moodTrackerDao: MoodTrackerDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MoodTrackerDb::class.java,
        ).build()
        moodTrackerDao = database.moodTrackerDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testGetAllMoods() = runBlocking {
        val now = LocalDate.now().toEpochDay()
        val mood1 = Mood(1, now, "emoji")
        val mood2 = Mood(2, now, "emoji")
        moodTrackerDao.insert(mood1)
        moodTrackerDao.insert(mood2)
        assertThat(moodTrackerDao.getAllMoods().size).isEqualTo(2)
    }

    @Test
    fun testInsertMood() = runBlocking {
        val now = LocalDate.now().toEpochDay()
        val mood = Mood(1, now, "emoji")
        moodTrackerDao.insert(mood)
        assertThat(moodTrackerDao.getAllMoods()[0]).isEqualTo(mood)
    }

    @Test
    fun testUpdateMood() = runBlocking {
        val now = LocalDate.now().toEpochDay()
        val mood = Mood(1, now, "emoji")
        moodTrackerDao.insert(mood)
        moodTrackerDao.update(mood.copy(mood = "mood2"))
        assertThat(moodTrackerDao.getAllMoods()[0]).isNotEqualTo(mood)
    }

    @Test
    fun testPeriodStatistics() = runBlocking {
        val today = LocalDate.now().toEpochDay()
        val yesterday = LocalDate.now().minusDays(1).toEpochDay()
        val mood1 = Mood(1, yesterday, "emoji")
        val mood2 = Mood(2, today, "emoji")
        moodTrackerDao.insert(mood1)
        moodTrackerDao.insert(mood2)
        val stats = moodTrackerDao.periodStatistics(today, today)
        assertThat(stats[0].mood).isEqualTo(mood2.mood)
        assertThat(stats[0].quantity).isEqualTo(1)
        assertThat(stats[0].percent).isEqualTo(100f)
    }

    @Test
    fun testAllTimeStatistics() = runBlocking {
        val today = LocalDate.now().toEpochDay()
        val yesterday = LocalDate.now().minusDays(1).toEpochDay()
        val mood1 = Mood(1, yesterday, "emoji")
        val mood2 = Mood(2, today, "emoji")
        moodTrackerDao.insert(mood1)
        moodTrackerDao.insert(mood2)
        val stats = moodTrackerDao.allTimeStatistics()
        assertThat(stats[0].mood).isEqualTo(mood1.mood)
        assertThat(stats[0].quantity).isEqualTo(2)
        assertThat(stats[0].percent).isEqualTo(100f)
    }
}