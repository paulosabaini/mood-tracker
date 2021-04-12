package org.sabaini.moodtracker.viewmodel

import android.view.View
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sabaini.moodtracker.db.Statistics
import org.sabaini.moodtracker.repository.MoodTrackerRepository
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(private val moodTrackerRepository: MoodTrackerRepository) :
    ViewModel() {

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
}