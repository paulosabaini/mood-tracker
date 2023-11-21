package org.sabaini.moodtracker.presentation.screens.calendar

import androidx.lifecycle.*
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sabaini.moodtracker.domain.model.Mood
import org.sabaini.moodtracker.domain.repository.MoodTrackerRepository
import org.sabaini.moodtracker.presentation.screens.calendar.Emojis.EMOJIS
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(private val moodTrackerRepositoryImpl: MoodTrackerRepository) :
    ViewModel() {

    private val _today = MutableLiveData<LocalDate>()
    val today: LiveData<LocalDate>
        get() = _today

    private val _displayYear = MutableLiveData<Year>()
    val displayYear: LiveData<Year>
        get() = _displayYear

    private val _emojisList = MutableLiveData<List<String>>()
    val emojiList: LiveData<List<String>>
        get() = _emojisList

    private val _moods = MutableLiveData<List<Mood>>()
    val moods: LiveData<List<Mood>>
        get() = _moods

    init {
        viewModelScope.launch {
            _moods.value = moodTrackerRepositoryImpl.getMoods()
        }
        _today.value = LocalDate.now()
        _displayYear.value = Year.now()
        _emojisList.value = EMOJIS
    }

    fun decrementDisplayYear() {
        _displayYear.value = _displayYear.value!!.minusYears(1L)
    }

    fun incrementDisplayYear() {
        _displayYear.value = _displayYear.value!!.plusYears(1L)
    }

    fun saveMood(mood: CharSequence) {
        viewModelScope.launch {
            if (mood.isEmpty()) {
                return@launch
            }
            if (moods.value!!.isEmpty()) {
                moodTrackerRepositoryImpl.insertMood(_today.value!!, mood)
            } else {
                val lastMood = moods.value!!.lastOrNull()
                if (lastMood!!.date != _today.value!!.toEpochDay()) {
                    moodTrackerRepositoryImpl.insertMood(_today.value!!, mood)
                } else {
                    moodTrackerRepositoryImpl.updateMood(
                        Mood(
                            id = lastMood.id,
                            date = lastMood.date,
                            mood = mood as String,
                        ),
                    )
                }
            }
            _moods.value = moodTrackerRepositoryImpl.getMoods()
        }
    }

    fun getDayText(date: LocalDate): Pair<String, Float> {
        val mood = moods.value?.find { mood ->
            date.toEpochDay() == mood.date
        }
        return if (mood != null) {
            Pair(mood.mood, 32f)
        } else {
            Pair(date.dayOfMonth.toString(), 16f)
        }
    }

    fun shouldDisplayEmojiPicker(data: CalendarDay) =
        data.position == DayPosition.MonthDate && _today.value == data.date

    fun getMonthName(data: CalendarMonth) = data.yearMonth.month.name.uppercase(Locale.getDefault())

    fun isCurrentYear(year: Year) = year.value == YearMonth.now().year

    fun isToday(data: CalendarDay) = data.date == _today.value

    fun isWeekend(data: CalendarDay) =
        data.date.dayOfWeek == DayOfWeek.SATURDAY || data.date.dayOfWeek == DayOfWeek.SUNDAY
}
