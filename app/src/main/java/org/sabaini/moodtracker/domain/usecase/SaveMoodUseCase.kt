package org.sabaini.moodtracker.domain.usecase

import org.sabaini.moodtracker.domain.model.Mood
import org.sabaini.moodtracker.domain.repository.MoodTrackerRepository
import java.time.LocalDate
import javax.inject.Inject

class SaveMoodUseCase @Inject constructor(
    private val repository: MoodTrackerRepository
) {
    suspend operator fun invoke(today: LocalDate, moods: List<Mood>?, mood: CharSequence) {
        if (mood.isEmpty()) {
            return
        }
        
        val lastMood = moods?.lastOrNull()
        if (lastMood?.date == today.toEpochDay()) {
            repository.updateMood(
                Mood(
                    id = lastMood.id,
                    date = lastMood.date,
                    mood = mood as String,
                ),
            )
        } else {
            repository.insertMood(today, mood)
        }
    }
}
