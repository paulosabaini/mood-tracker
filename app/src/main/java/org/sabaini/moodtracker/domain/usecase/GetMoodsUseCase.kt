package org.sabaini.moodtracker.domain.usecase

import org.sabaini.moodtracker.domain.model.Mood
import org.sabaini.moodtracker.domain.repository.MoodTrackerRepository
import javax.inject.Inject

class GetMoodsUseCase @Inject constructor(
    private val repository: MoodTrackerRepository
) {
    suspend operator fun invoke(): List<Mood>? {
        return repository.getMoods()
    }
}
