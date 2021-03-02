package org.sabaini.moodtracker.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.sabaini.moodtracker.db.getDatabase
import org.sabaini.moodtracker.repository.MoodTrackerRepository
import java.time.LocalDate
import java.time.Year

class CalendarViewModel(application: Application) : ViewModel() {

    /* Database and Repository variables */
    private val database = getDatabase(application)
    private val memesRepository = MoodTrackerRepository(database)

    private val _selectedDate = MutableLiveData<LocalDate>()
    val selectedDate: LiveData<LocalDate>
        get() = _selectedDate

    private val _today = MutableLiveData<LocalDate>()
    val today: LiveData<LocalDate>
        get() = _today

    private val _displayYear = MutableLiveData<Year>()
    val displayYear: LiveData<Year>
        get() = _displayYear

    private val _emojisList = MutableLiveData<List<String>>()
    val emojiList: LiveData<List<String>>
        get() = _emojisList

    init {
        _today.value = LocalDate.now()
        _displayYear.value = Year.now()
        _selectedDate.value = LocalDate.now()
//        _emojisList.value = listOf(
//            0x1F622,
//            0x1F641,
//            0x1F610,
//            0x1F642,
//            0x1F601,
//            0x1F634,
//            0x1F60D,
//            0x1F92A,
//            0x1F973,
//            0x1F60E,
//            0x1F631,
//            0x1F912,
//            0x1F92F,
//            0x1F620,
//            0x1F92C
//        )
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
            memesRepository.insertMood(_selectedDate.value!!, mood)
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CalendarViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CalendarViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}