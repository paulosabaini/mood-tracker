package org.sabaini.moodtracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.Year

class CalendarViewModel : ViewModel() {

    private val _selectedDate = MutableLiveData<LocalDate>()
    val selectedDate: LiveData<LocalDate>
        get() = _selectedDate

    private val _today = MutableLiveData<LocalDate>()
    val today: LiveData<LocalDate>
        get() = _today

    private val _displayYear = MutableLiveData<Year>()
    val displayYear: LiveData<Year>
        get() = _displayYear

    private val _emojisList = MutableLiveData<List<Int>>()
    val emojiList: LiveData<List<Int>>
        get() = _emojisList

    init {
        _today.value = LocalDate.now()
        _displayYear.value = Year.now()
        _selectedDate.value = LocalDate.now()
        _emojisList.value = listOf(
            0x1F622,
            0x1F641,
            0x1F610,
            0x1F642,
            0x1F601,
            0x1F634,
            0x1F60D,
            0x1F92A,
            0x1F973,
            0x1F60E,
            0x1F631,
            0x1F912,
            0x1F92F,
            0x1F620,
            0x1F92C
        )
    }

    fun decrementDisplayYear() {
        _displayYear.value = _displayYear.value!!.minusYears(1L)
    }

    fun incrementDisplayYear() {
        _displayYear.value = _displayYear.value!!.plusYears(1L)
    }
}