package org.sabaini.moodtracker.presentation.screens.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sabaini.moodtracker.domain.model.Statistics
import org.sabaini.moodtracker.domain.repository.MoodTrackerRepository
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(private val moodTrackerRepositoryImpl: MoodTrackerRepository) :
    ViewModel() {

    private val _statistics = MutableLiveData<List<Statistics>>()
    val statistics: LiveData<List<Statistics>> = _statistics

    private val _filter = MutableLiveData<StatisticFilterType>()
    val filter: LiveData<StatisticFilterType> = _filter

    companion object {
        private const val FIRST_DAY = 1
    }

    init {
        viewModelScope.launch {
            _statistics.value = moodTrackerRepositoryImpl.getStatistics(
                LocalDate.now().withDayOfMonth(FIRST_DAY).toEpochDay(),
                LocalDate.now().toEpochDay(),
            )
        }
    }

    fun filterClick(type: StatisticFilterType) {
        _filter.value = type
        filterStatistics()
    }

    private fun filterStatistics() {
        val now = LocalDate.now()
        when (_filter.value) {
            StatisticFilterType.WEEK -> {
                val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
                val lastDayOfWeek =
                    DayOfWeek.of(((firstDayOfWeek.value + 5) % DayOfWeek.values().size) + 1)

                updateStatistics(
                    now.with(TemporalAdjusters.previousOrSame(firstDayOfWeek)).toEpochDay(),
                    now.with(TemporalAdjusters.nextOrSame(lastDayOfWeek)).toEpochDay(),
                )
            }

            StatisticFilterType.MONTH -> {
                updateStatistics(
                    now.withDayOfMonth(FIRST_DAY).toEpochDay(),
                    now.withDayOfMonth(now.lengthOfMonth()).toEpochDay(),
                )
            }

            StatisticFilterType.YEAR -> {
                updateStatistics(
                    now.withDayOfYear(FIRST_DAY).toEpochDay(),
                    now.withDayOfYear(now.lengthOfYear()).toEpochDay(),
                )
            }

            StatisticFilterType.ALL -> {
                updateStatistics(null, null)
            }

            else -> {
                updateStatistics(null, null)
            }
        }
    }

    private fun updateStatistics(begin: Long?, end: Long?) {
        viewModelScope.launch {
            _statistics.value = moodTrackerRepositoryImpl.getStatistics(begin, end)
        }
    }
}
