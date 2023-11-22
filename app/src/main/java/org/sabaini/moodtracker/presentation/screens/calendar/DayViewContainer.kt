package org.sabaini.moodtracker.presentation.screens.calendar

import android.view.View
import com.kizitonwose.calendar.view.ViewContainer
import org.sabaini.moodtracker.databinding.CalendarDayLayoutBinding

class DayViewContainer(view: View) : ViewContainer(view) {
    val textView = CalendarDayLayoutBinding.bind(view).calendarDayText
    var onClick: () -> Unit = {}

    init {
        textView.setOnClickListener { onClick() }
    }
}