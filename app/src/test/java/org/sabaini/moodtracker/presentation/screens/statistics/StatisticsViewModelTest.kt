package org.sabaini.moodtracker.presentation.screens.statistics

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.sabaini.moodtracker.MainCoroutineRule
import org.sabaini.moodtracker.presentation.screens.statistics.StatisticsViewModel
import org.sabaini.moodtracker.data.repository.FakeMoodTrackerRepository
import java.time.LocalDate


class StatisticsViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: StatisticsViewModel
    private lateinit var repository: FakeMoodTrackerRepository

    @Before
    fun setup() {
        repository = FakeMoodTrackerRepository()
        viewModel = StatisticsViewModel(repository)
    }

    @Test
    fun testUpdateStatistics() = runBlocking {
        val now = LocalDate.now()
        repository.insertMood(now, "emoji")
        viewModel.updateStatistics(now.toEpochDay(), now.toEpochDay())
        assertThat(viewModel.databaseStatistics.value).isNotEmpty()
    }

    @Test
    fun testUpdateStatisticsTwoDays() = runBlocking {
        val now = LocalDate.now()
        val yesterday = now.minusDays(1)
        repository.insertMood(yesterday, "emoji")
        repository.insertMood(now, "emoji2")
        viewModel.updateStatistics(yesterday.toEpochDay(), now.toEpochDay())
        assertThat(viewModel.databaseStatistics.value!!.size).isEqualTo(2)
    }
}