package org.sabaini.moodtracker.presentation.screens.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sabaini.moodtracker.domain.model.Statistics
import org.sabaini.moodtracker.domain.usecase.GetStatisticsUseCase
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getStatisticsUseCase: GetStatisticsUseCase
) : ViewModel() {

    private val _statistics = MutableLiveData<List<Statistics>>()
    val statistics: LiveData<List<Statistics>> = _statistics

    private val _filter = MutableLiveData<StatisticFilterType>()
    val filter: LiveData<StatisticFilterType> = _filter

    init {
        viewModelScope.launch {
            _statistics.value = getStatisticsUseCase(null)
        }
    }

    fun filterClick(type: StatisticFilterType) {
        _filter.value = type
        filterStatistics()
    }

    private fun filterStatistics() {
        viewModelScope.launch {
            _statistics.value = getStatisticsUseCase(_filter.value)
        }
    }
}
