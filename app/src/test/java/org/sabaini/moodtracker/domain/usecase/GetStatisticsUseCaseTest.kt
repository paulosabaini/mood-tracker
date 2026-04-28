package org.sabaini.moodtracker.domain.usecase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.sabaini.moodtracker.data.repository.FakeMoodTrackerRepository
import org.sabaini.moodtracker.presentation.screens.statistics.StatisticFilterType
import java.time.LocalDate

class GetStatisticsUseCaseTest {

    private lateinit var getStatisticsUseCase: GetStatisticsUseCase
    private lateinit var repository: FakeMoodTrackerRepository

    @Before
    fun setup() {
        repository = FakeMoodTrackerRepository()
        getStatisticsUseCase = GetStatisticsUseCase(repository)
    }

    @Test
    fun `invoke with month filter should return statistics`() = runBlocking {
        repository.insertMood(LocalDate.now(), "😊")
        val stats = getStatisticsUseCase(StatisticFilterType.MONTH)
        assertThat(stats).isNotEmpty()
    }
    
    @Test
    fun `invoke with null filter should return default statistics`() = runBlocking {
        repository.insertMood(LocalDate.now(), "😊")
        val stats = getStatisticsUseCase(null)
        assertThat(stats).isNotEmpty()
    }
}
