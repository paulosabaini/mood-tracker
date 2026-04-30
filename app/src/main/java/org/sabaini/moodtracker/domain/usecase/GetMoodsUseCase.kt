package org.sabaini.moodtracker.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.sabaini.moodtracker.domain.model.Mood
import org.sabaini.moodtracker.domain.repository.MoodTrackerRepository
import javax.inject.Inject

class GetMoodsUseCase @Inject constructor(
    private val repository: MoodTrackerRepository
) {
    operator fun invoke(): Flow<List<Mood>?> {
        return repository.getMoods()
    }
}
