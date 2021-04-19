package org.sabaini.moodtracker.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.sabaini.moodtracker.MainCoroutineRule
import org.sabaini.moodtracker.repositories.FakeMoodTrackerRepository
import java.time.LocalDate

class CalendarViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: CalendarViewModel
    private lateinit var repository: FakeMoodTrackerRepository

    @Before
    fun setup() {
        repository = FakeMoodTrackerRepository()
        viewModel = CalendarViewModel(repository)
    }

    @Test
    fun testDecrementDisplayYear() {
        val year = viewModel.displayYear.value!!.value
        viewModel.decrementDisplayYear()
        assertThat(viewModel.displayYear.value!!.value).isEqualTo(year - 1)
    }

    @Test
    fun testIncrementDisplayYear() {
        val year = viewModel.displayYear.value!!.value
        viewModel.incrementDisplayYear()
        assertThat(viewModel.displayYear.value!!.value).isEqualTo(year + 1)
    }

    @Test
    fun testSaveMood() {
        val mood = "emoji"
        viewModel.saveMood(mood)
        assertThat(viewModel.moods.value!![0].mood).isEqualTo(mood)
    }

    @Test
    fun testPopulatedSaveMood() = runBlocking {
        repository.insertMood(LocalDate.now().minusDays(1), "emoji1")
        val mood = "emoji2"
        viewModel.saveMood(mood)
        assertThat(viewModel.moods.value!![1].mood).isEqualTo(mood)
    }

    @Test
    fun testSaveEmptyMood() {
        val mood = ""
        viewModel.saveMood(mood)
        assertThat(viewModel.moods.value!!).isEmpty()
    }

    @Test
    fun testSaveUpdatedMood() {
        val mood = "emoji"
        val mood2 = mood + "2"
        viewModel.saveMood(mood)
        viewModel.saveMood(mood2)
        assertThat(viewModel.moods.value!![0].mood).isEqualTo(mood2)
    }

    @Test
    fun testFilterMoods() {
        val mood = "emoji"
        viewModel.saveMood(mood)
        assertThat(viewModel.filterMoods()).isNotEmpty()
    }
}