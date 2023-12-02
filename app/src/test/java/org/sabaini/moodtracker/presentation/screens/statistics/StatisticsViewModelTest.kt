package org.sabaini.moodtracker.presentation.screens.statistics

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.sabaini.moodtracker.MainCoroutineRule
import org.sabaini.moodtracker.data.repository.FakeMoodTrackerRepository
import java.time.LocalDate

@RunWith(MockitoJUnitRunner::class)
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

    @Test()
    fun shouldFilterTheStatistics() = runBlocking {
        val now = LocalDate.now()
        repository.insertMood(now, "emoji")
        viewModel.filterClick(StatisticFilterType.MONTH)
        assertThat(viewModel.statistics.value).isNotEmpty()
    }
}
