package org.sabaini.moodtracker.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.sabaini.moodtracker.MainCoroutineRule
import org.sabaini.moodtracker.repository.FakeMoodTrackerRepository
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
        assertThat(viewModel.statistics.value).isNotEmpty()
    }
}