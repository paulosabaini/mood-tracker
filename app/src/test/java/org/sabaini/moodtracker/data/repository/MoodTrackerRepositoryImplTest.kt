package org.sabaini.moodtracker.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.sabaini.moodtracker.MainCoroutineRule
import org.sabaini.moodtracker.data.local.dao.MoodDao
import org.sabaini.moodtracker.data.local.model.DatabaseStatistics
import org.sabaini.moodtracker.data.local.model.MoodEntity
import org.sabaini.moodtracker.domain.model.Mood
import java.time.LocalDate

@RunWith(MockitoJUnitRunner::class)
class MoodTrackerRepositoryImplTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var dao: MoodDao

    private lateinit var repository: MoodTrackerRepositoryImpl
    private lateinit var data: MutableList<MoodEntity>
    private val statistics = mutableListOf<DatabaseStatistics>()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = MoodTrackerRepositoryImpl(dao)
        data = mutableListOf()
    }

    @Test
    fun testInsertMood() = runBlocking {
        val now = LocalDate.now()
        val m = MoodEntity(null, now.toEpochDay(), "emoji")
        Mockito.`when`(dao.saveMood(m)).then {
            data.add(m)
        }

        repository.insertMood(now, m.mood)
        assertThat(data).isNotEmpty()
    }

    @Test
    fun testGetMoods() = runBlocking {
        val now = LocalDate.now()
        val d1 = MoodEntity(1, now.toEpochDay(), "emoji")
        val d2 = MoodEntity(2, now.toEpochDay(), "emoji2")
        data.add(d1)
        data.add(d2)
        Mockito.`when`(dao.getAllMoods()).thenAnswer { data }
        assertThat(repository.getMoods()?.size).isEqualTo(data.size)
    }

    @Test
    fun testUpdateMood() = runBlocking {
        val now = LocalDate.now()
        val m = Mood(1, now.toEpochDay(), "emoji")
        val dm = MoodEntity(1, now.toEpochDay(), "emoji")
        Mockito.`when`(dao.saveMood(dm)).then {
            data.add(dm)
        }
        repository.updateMood(m)
        assertThat(data[0].mood).isEqualTo(m.mood)
    }

    @Test
    fun testGetStatisticsWithoutPeriod() = runBlocking {
        val now = LocalDate.now()
        val d1 = MoodEntity(1, now.toEpochDay(), "emoji")
        val d2 = MoodEntity(2, now.toEpochDay(), "emoji2")
        data.add(d1)
        data.add(d2)

        Mockito.`when`(dao.allTimeStatistics()).thenAnswer {
            getDatabaseStatistics()
        }
        val stats = repository.getStatistics(null, null)
        assertThat(stats).isNotEmpty()
    }

    @Test
    fun testGetStatisticsWithPeriod() = runBlocking {
        val now = LocalDate.now()
        val d1 = MoodEntity(1, now.minusDays(1).toEpochDay(), "emoji")
        val d2 = MoodEntity(2, now.toEpochDay(), "emoji2")
        data.add(d1)
        data.add(d2)

        Mockito.`when`(dao.periodStatistics(now.toEpochDay(), now.toEpochDay())).thenAnswer {
            getDatabaseStatistics()
        }
        val stats = repository.getStatistics(now.toEpochDay(), now.toEpochDay())
        assertThat(stats).isNotEmpty()
    }

    private fun getDatabaseStatistics(): List<DatabaseStatistics> {
        data.forEach { mood ->
            val stat = statistics.find { statistic -> mood.mood == statistic.mood }
            if (stat == null) {
                val newStat = DatabaseStatistics(mood.mood, 1, 100f)
                statistics.add(newStat)
            } else {
                val upStat = DatabaseStatistics(
                    mood = stat.mood,
                    quantity = stat.quantity!!.plus(1),
                    percent = stat.percent!! / 2,
                )
                statistics.remove(stat)
                statistics.add(upStat)
            }
        }
        return statistics
    }
}
