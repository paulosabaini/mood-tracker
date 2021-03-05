package org.sabaini.moodtracker.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.sabaini.moodtracker.db.Statistics
import org.sabaini.moodtracker.db.getDatabase
import org.sabaini.moodtracker.repository.MoodTrackerRepository
import java.time.LocalDate

class StatisticsViewModel(application: Application) : ViewModel() {

    /* Database and Repository variables */
    private val database = getDatabase(application)
    private val moodTrackerRepository = MoodTrackerRepository(database)

    private val _monthStatistics = MutableLiveData<List<Statistics>>()
    val monthStatistics: LiveData<List<Statistics>>
        get() = _monthStatistics

    private val _filter = MutableLiveData<View>()
    val filter: LiveData<View>
        get() = _filter

    init {
        viewModelScope.launch {
            _monthStatistics.value = moodTrackerRepository.getStatistics(
                LocalDate.now().withDayOfMonth(1).toEpochDay(),
                LocalDate.now().toEpochDay()
            )
        }
    }

    fun filterClick(view: View) {
        _filter.value = view
    }

    fun updateStatistics(begin: Long?, end: Long?) {
        viewModelScope.launch {
            _monthStatistics.value = moodTrackerRepository.getStatistics(begin, end)
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StatisticsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return StatisticsViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}