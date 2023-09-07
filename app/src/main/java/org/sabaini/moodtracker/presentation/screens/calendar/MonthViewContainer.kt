package org.sabaini.moodtracker.presentation.screens.calendar

import android.view.View
import com.kizitonwose.calendar.view.ViewContainer
import org.sabaini.moodtracker.databinding.CalendarDayTitlesContainerBinding

class MonthViewContainer(view: View) : ViewContainer(view) {
    val textView = CalendarDayTitlesContainerBinding.bind(view).calendarHeaderText
}
