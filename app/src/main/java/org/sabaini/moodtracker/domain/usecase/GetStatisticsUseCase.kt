package org.sabaini.moodtracker.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.sabaini.moodtracker.domain.model.Statistics
import org.sabaini.moodtracker.domain.repository.MoodTrackerRepository
import org.sabaini.moodtracker.presentation.screens.statistics.StatisticFilterType
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Locale
import javax.inject.Inject

class GetStatisticsUseCase @Inject constructor(
    private val repository: MoodTrackerRepository
) {
    companion object {
        private const val FIRST_DAY = 1
    }

    operator fun invoke(filter: StatisticFilterType?): Flow<List<Statistics>?> {
        val now = LocalDate.now()
        val (begin, end) = when (filter) {
            StatisticFilterType.WEEK -> {
                val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
                val lastDayOfWeek =
                    DayOfWeek.of(((firstDayOfWeek.value + 5) % DayOfWeek.values().size) + 1)

                Pair(
                    now.with(TemporalAdjusters.previousOrSame(firstDayOfWeek)).toEpochDay(),
                    now.with(TemporalAdjusters.nextOrSame(lastDayOfWeek)).toEpochDay()
                )
            }

            StatisticFilterType.MONTH -> {
                Pair(
                    now.withDayOfMonth(FIRST_DAY).toEpochDay(),
                    now.withDayOfMonth(now.lengthOfMonth()).toEpochDay()
                )
            }

            StatisticFilterType.YEAR -> {
                Pair(
                    now.withDayOfYear(FIRST_DAY).toEpochDay(),
                    now.withDayOfYear(now.lengthOfYear()).toEpochDay()
                )
            }

            StatisticFilterType.ALL -> {
                Pair(null, null)
            }

            else -> {
                // Default behavior from ViewModel init or else branch
                Pair(
                    now.withDayOfMonth(FIRST_DAY).toEpochDay(),
                    now.toEpochDay()
                )
            }
        }

        return repository.getStatistics(begin, end)
    }
}
