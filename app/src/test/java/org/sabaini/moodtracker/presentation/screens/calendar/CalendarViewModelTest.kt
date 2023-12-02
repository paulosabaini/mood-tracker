package org.sabaini.moodtracker.presentation.screens.calendar

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.sabaini.moodtracker.MainCoroutineRule
import org.sabaini.moodtracker.data.repository.FakeMoodTrackerRepository
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth

@RunWith(MockitoJUnitRunner::class)
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
    fun shouldReturnEmojiIfMoodExists() {
        val mood = "emoji"
        viewModel.saveMood(mood)
        assertThat(viewModel.getDayText(LocalDate.now()).first).isEqualTo(mood)
    }

    @Test
    fun shouldReturnDayIfMoodNotExists() {
        val date = LocalDate.now()
        assertThat(viewModel.getDayText(date).first).isEqualTo(date.dayOfMonth.toString())
    }

    @Test
    fun shouldDisplayEmojiPicker() {
        assertThat(
            viewModel.shouldDisplayEmojiPicker(
                CalendarDay(
                    date = LocalDate.now(),
                    position = DayPosition.MonthDate,
                ),
            ),
        ).isTrue()
    }

    @Test
    fun shouldNotDisplayEmojiPicker() {
        assertThat(
            viewModel.shouldDisplayEmojiPicker(
                CalendarDay(
                    date = LocalDate.now().minusDays(1L),
                    position = DayPosition.MonthDate,
                ),
            ),
        ).isFalse()
    }

    @Test
    fun shouldGetTheCorrectMonthName() {
        assertThat(
            viewModel.getMonthName(
                CalendarMonth(
                    yearMonth = YearMonth.of(2023, 11),
                    weekDays = emptyList(),
                ),
            ),
        ).isEqualTo("NOVEMBER")
    }

    @Test
    fun shouldReturnTrueIfItsTheCurrentYear() {
        assertThat(viewModel.isCurrentYear(Year.now())).isTrue()
    }

    @Test
    fun shouldReturnFalseIfItsNotTheCurrentYear() {
        assertThat(viewModel.isCurrentYear(Year.now().minusYears(1L))).isFalse()
    }

    @Test
    fun shouldReturnTrueIfItsToday() {
        assertThat(
            viewModel.isToday(
                CalendarDay(
                    date = LocalDate.now(),
                    position = DayPosition.MonthDate,
                ),
            ),
        ).isTrue()
    }

    @Test
    fun shouldReturnFalseIfItsNotToday() {
        assertThat(
            viewModel.isToday(
                CalendarDay(
                    date = LocalDate.now().minusDays(1L),
                    position = DayPosition.MonthDate,
                ),
            ),
        ).isFalse()
    }

    @Test
    fun shouldReturnTrueIfItsWeekend() {
        assertThat(
            viewModel.isWeekend(
                CalendarDay(
                    date = LocalDate.of(2023, 1, 1),
                    position = DayPosition.MonthDate,
                ),
            ),
        ).isTrue()
    }

    @Test
    fun shouldReturnFalseIfItsNotWeekend() {
        assertThat(
            viewModel.isWeekend(
                CalendarDay(
                    date = LocalDate.of(2023, 1, 2),
                    position = DayPosition.MonthDate,
                ),
            ),
        ).isFalse()
    }
}
