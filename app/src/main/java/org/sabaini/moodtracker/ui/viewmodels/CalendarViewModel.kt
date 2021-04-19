package org.sabaini.moodtracker.ui.viewmodels

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sabaini.moodtracker.entities.Mood
import org.sabaini.moodtracker.repositories.MoodTrackerRepository
import org.sabaini.moodtracker.utilities.Values.EMOJIS
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
                            mood = mood as String
                        )
                    )
                }
            }
            _moods.value = moodTrackerRepositoryImpl.getMoods()
        }
    }

    fun filterMoods(): List<Mood> {
        return _moods.value!!.filter { it.date >= _displayYear.value!!.atDay(1).toEpochDay() }
    }
}