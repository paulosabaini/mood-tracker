package org.sabaini.moodtracker.presentation.screens.calendar

import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import androidx.emoji2.text.EmojiCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sabaini.moodtracker.domain.model.Mood
import org.sabaini.moodtracker.domain.usecase.GetMoodsUseCase
import org.sabaini.moodtracker.domain.usecase.SaveMoodUseCase
import org.sabaini.moodtracker.presentation.screens.calendar.Emojis.EMOJIS
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getMoodsUseCase: GetMoodsUseCase,
    private val saveMoodUseCase: SaveMoodUseCase
) : ViewModel() {

    private val today = LocalDate.now()

    private val _displayYear = MutableLiveData(Year.now())
    val displayYear: LiveData<Year> = _displayYear

    val moods: LiveData<List<Mood>> = getMoodsUseCase().asLiveData() as LiveData<List<Mood>>

    companion object {
        private const val YEAR_INCREMENT = 1L
        private const val EMOJI_SIZE = 32f
        private const val DAY_TEXT_SIZE = 16f
        private const val ZERO = 0
    }

    fun decrementDisplayYear() {
        _displayYear.value = _displayYear.value?.minusYears(YEAR_INCREMENT)
    }

    fun incrementDisplayYear() {
        _displayYear.value = _displayYear.value?.plusYears(YEAR_INCREMENT)
    }

    fun saveMood(mood: CharSequence) {
        viewModelScope.launch {
            saveMoodUseCase(today, moods.value, mood)
        }
    }

    fun getDayText(date: LocalDate): Pair<String, Float> {
        val mood = moods.value?.find { mood ->
            date.toEpochDay() == mood.date
        }
        return if (mood != null) {
            Pair(mood.mood, EMOJI_SIZE)
        } else {
            Pair(date.dayOfMonth.toString(), DAY_TEXT_SIZE)
        }
    }

    fun shouldDisplayEmojiPicker(data: CalendarDay) =
        data.position == DayPosition.MonthDate && today == data.date

    fun getMonthName(data: CalendarMonth) = data.yearMonth.month.name.uppercase(Locale.getDefault())

    fun isCurrentYear(year: Year) = year.value == YearMonth.now().year

    fun isToday(data: CalendarDay) = data.date == today

    fun isWeekend(data: CalendarDay) =
        data.date.dayOfWeek == DayOfWeek.SATURDAY || data.date.dayOfWeek == DayOfWeek.SUNDAY

    fun getFormattedEmojiList(): List<SpannableString> {
        return EMOJIS.map {
            val emoji = SpannableString(EmojiCompat.get().process(it))
            emoji.setSpan(
                AbsoluteSizeSpan(EMOJI_SIZE.toInt(), true),
                ZERO,
                emoji.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
            emoji
        }
    }
}
