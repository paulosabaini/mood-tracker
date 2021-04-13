package org.sabaini.moodtracker.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sabaini.moodtracker.db.Mood
import org.sabaini.moodtracker.repository.MoodTrackerRepository
import org.sabaini.moodtracker.repository.MoodTrackerRepositoryImpl
import java.time.LocalDate
import java.time.Year
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
        _emojisList.value = listOf(
            "\ud83d\ude22",
            "\ud83d\ude41",
            "\ud83d\ude10",
            "\ud83d\ude42",
            "\ud83d\ude01",
            "\ud83d\ude34",
            "\ud83d\ude0d",
            "\ud83e\udd2a",
            "\ud83e\udd73",
            "\ud83d\ude0e",
            "\ud83d\ude31",
            "\ud83e\udd12",
            "\ud83e\udd2f",
            "\ud83d\ude20",
            "\ud83e\udd2c"
        )
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
                    moodTrackerRepositoryImpl.updateMood(lastMood.copy(mood = mood as String))
                }
            }
            _moods.value = moodTrackerRepositoryImpl.getMoods()
        }
    }

    fun filterMoods(): List<Mood> {
        return _moods.value!!.filter { it.date >= _displayYear.value!!.atDay(1).toEpochDay() }
    }
}