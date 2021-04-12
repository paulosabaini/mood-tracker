package org.sabaini.moodtracker.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.util.TypedValue
import android.view.*
import android.view.LayoutInflater
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.emoji.text.EmojiCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import dagger.hilt.android.AndroidEntryPoint
import org.sabaini.moodtracker.R
import org.sabaini.moodtracker.databinding.FragmentCalendarBinding
import org.sabaini.moodtracker.viewmodel.CalendarViewModel
import java.time.DayOfWeek
import java.time.YearMonth
import java.util.*

@AndroidEntryPoint
class CalendarFragment : Fragment() {

    private val viewModel: CalendarViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCalendarBinding.inflate(layoutInflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        // Start the calendar
        setupCalendar(binding.calendarView)

        // Set the calendar to the previous year
        binding.previousYear.setOnClickListener {
            viewModel.decrementDisplayYear()
            setupCalendar(binding.calendarView)
        }

        // Set the calendar to the next year
        binding.nextYear.setOnClickListener {
            viewModel.incrementDisplayYear()
            setupCalendar(binding.calendarView)
        }

        /*
         * Create the view container which acts as a view holder for each date cell.
         * The view passed in here is the inflated day view resource calendar_day_layout.xml.
         */
        class DayViewContainer(view: View) : ViewContainer(view) {
            // Will be set when this container is bound. See the dayBinder.
            lateinit var day: CalendarDay

            val textView = view.findViewById<TextView>(R.id.calendarDayText)

            init {
                textView.setOnClickListener {
                    // Check the day owner as we do not want to select in or out dates.
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (viewModel.today.value == day.date) {
                            showMenu(textView)
                        }
                    }
                }
            }
        }

        /*
         * Provide a DayBinder for the CalendarView using your DayViewContainer type.
         */
        binding.calendarView.dayBinder =
            object : DayBinder<DayViewContainer> {
                // Called only when a new container is needed.
                override fun create(view: View) = DayViewContainer(view)

                // Called every time we need to reuse a container.
                override fun bind(container: DayViewContainer, day: CalendarDay) {
                    container.day = day
                    val textView = container.textView
                    textView.text = day.date.dayOfMonth.toString()

                    viewModel.moods.observe(
                        viewLifecycleOwner,
                        {
                            val moods = viewModel.filterMoods()
                            val mood = moods.find { mood -> day.date.toEpochDay() == mood.date }
                            if (mood != null) {
                                textView.text = mood.mood
                                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32f)
                            }
                        })

                    if (day.owner == DayOwner.THIS_MONTH) {
                        textView.visibility = View.VISIBLE
                        when (day.date) {
                            viewModel.today.value -> {
                                textView.setTextColor(
                                    ContextCompat.getColor(
                                        context!!,
                                        R.color.ink
                                    )
                                )
                                textView.setBackgroundResource(R.drawable.day_selected_background)
                            }
                            else -> {
                                textView.setTextColor(
                                    ContextCompat.getColor(
                                        context!!,
                                        R.color.ink
                                    )
                                )
                                textView.setBackgroundResource(R.drawable.day_background)
                            }
                        }

                        val dayOfWeek = day.date.dayOfWeek
                        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                            textView.setTextColor(
                                ContextCompat.getColor(
                                    context!!,
                                    R.color.red
                                )
                            )
                            textView.setBackgroundResource(R.drawable.day_weekend_background)
                        }

                    } else {
                        textView.visibility = View.INVISIBLE
                    }
                }
            }

        /*
         * Create the view container which acts as a view holder for each month header.
         * The view passed in here is the inflated month header view resource calendar_header_layout.xml.
         */
        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = view.findViewById<TextView>(R.id.calendarHeaderText)
        }

        /*
        * Provide a MonthHeaderFooterBinder for the CalendarView using your MonthViewContainer type.
        */
        binding.calendarView.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {

                override fun create(view: View) = MonthViewContainer(view)

                override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                    @SuppressLint("SetTextI18n") // Concatenation warning for `setText` call.
                    container.textView.text =
                        month.yearMonth.month.name.toUpperCase(Locale.getDefault())
                }
            }

        return binding.root
    }

    /*
    * Setup the Calendar View
    */
    private fun setupCalendar(calendar: CalendarView) {
        val year = viewModel.displayYear.value

        calendar.setup(
            YearMonth.of(year!!.value, 1),
            YearMonth.of(year.value, 12),
            DayOfWeek.SUNDAY
        )

        if (year.value == YearMonth.now().year) {
            calendar.scrollToMonth(YearMonth.now())
        }
    }

    /*
    * Display a popup menu with emojis that can be selected
    */
    private fun showMenu(v: View) {
        val popup = PopupMenu(context, v)

        viewModel.emojiList.value!!.forEach {
            val emoji = SpannableString(EmojiCompat.get().process(it))
            emoji.setSpan(
                AbsoluteSizeSpan(32, true),
                0,
                emoji.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            popup.menu.add(emoji)
        }

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            (v as TextView).text = menuItem.title
            v.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32f)
            viewModel.saveMood(menuItem.title.toString())
            true
        }

        // Show the popup menu.
        popup.show()
    }
}