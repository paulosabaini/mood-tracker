package org.sabaini.moodtracker.domain.usecase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.sabaini.moodtracker.data.repository.FakeMoodTrackerRepository
import java.time.LocalDate

class GetMoodsUseCaseTest {

    private lateinit var getMoodsUseCase: GetMoodsUseCase
    private lateinit var repository: FakeMoodTrackerRepository

    @Before
    fun setup() {
        repository = FakeMoodTrackerRepository()
        getMoodsUseCase = GetMoodsUseCase(repository)
    }

    @Test
    fun `invoke should return moods from repository`() = runBlocking {
        repository.insertMood(LocalDate.now(), "😊")
        val moods = getMoodsUseCase().first()
        assertThat(moods).isNotEmpty()
        assertThat(moods!![0].mood).isEqualTo("😊")
    }
}
