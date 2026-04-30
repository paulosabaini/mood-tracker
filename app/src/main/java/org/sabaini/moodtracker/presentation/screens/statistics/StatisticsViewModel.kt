package org.sabaini.moodtracker.presentation.screens.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import org.sabaini.moodtracker.domain.model.Statistics
import org.sabaini.moodtracker.domain.usecase.GetStatisticsUseCase
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getStatisticsUseCase: GetStatisticsUseCase
) : ViewModel() {

    private val _filter = MutableLiveData<StatisticFilterType>(StatisticFilterType.ALL)
    val filter: LiveData<StatisticFilterType> = _filter

    val statistics: LiveData<List<Statistics>> = _filter.asFlow().flatMapLatest { type ->
        getStatisticsUseCase(type)
    }.asLiveData() as LiveData<List<Statistics>>

    fun filterClick(type: StatisticFilterType) {
        _filter.value = type
    }
}
