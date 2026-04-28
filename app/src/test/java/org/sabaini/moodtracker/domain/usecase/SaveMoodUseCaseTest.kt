package org.sabaini.moodtracker.domain.usecase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.sabaini.moodtracker.data.repository.FakeMoodTrackerRepository
import java.time.LocalDate

class SaveMoodUseCaseTest {

    private lateinit var saveMoodUseCase: SaveMoodUseCase
    private lateinit var repository: FakeMoodTrackerRepository

    @Before
    fun setup() {
        repository = FakeMoodTrackerRepository()
        saveMoodUseCase = SaveMoodUseCase(repository)
    }

    @Test
    fun `invoke should insert mood if none exists for today`() = runBlocking {
        val today = LocalDate.now()
        saveMoodUseCase(today, emptyList(), "😊")
        val moods = repository.getMoods()
        assertThat(moods).hasSize(1)
        assertThat(moods[0].mood).isEqualTo("😊")
    }

    @Test
    fun `invoke should update mood if one already exists for today`() = runBlocking {
        val today = LocalDate.now()
        repository.insertMood(today, "😢")
        val existingMoods = repository.getMoods()
        
        saveMoodUseCase(today, existingMoods, "😊")
        
        val updatedMoods = repository.getMoods()
        assertThat(updatedMoods).hasSize(1)
        assertThat(updatedMoods[0].mood).isEqualTo("😊")
    }

    @Test
    fun `invoke should not insert if mood is empty`() = runBlocking {
        val today = LocalDate.now()
        saveMoodUseCase(today, emptyList(), "")
        val moods = repository.getMoods()
        assertThat(moods).isEmpty()
    }
}
