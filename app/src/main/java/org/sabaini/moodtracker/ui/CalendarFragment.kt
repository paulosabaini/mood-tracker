package org.sabaini.moodtracker.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.LayoutInflater
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.emoji.text.EmojiCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import org.sabaini.moodtracker.R
import org.sabaini.moodtracker.databinding.FragmentCalendarBinding
import org.sabaini.moodtracker.viewmodel.CalendarViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import java.util.*


class CalendarFragment : Fragment() {

    private val viewModel: CalendarViewModel by lazy {
        ViewModelProvider(this).get(CalendarViewModel::class.java)
    }

    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()
    private var displayYear = Year.now()

    private val emojisList = listOf(
        0x1F622,
        0x1F641,
        0x1F610,
        0x1F642,
        0x1F601,
        0x1F634,
        0x1F60D,
        0x1F92A,
        0x1F973,
        0x1F60E,
        0x1F631,
        0x1F912,
        0x1F92F,
        0x1F620,
        0x1F92C
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCalendarBinding.inflate(layoutInflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setupCalendar(binding.calendarView)

        binding.previousYear.setOnClickListener {
            displayYear = displayYear.minusYears(1)
            binding.currentYear.text = displayYear.value.toString()
            setupCalendar(binding.calendarView)
        }

        binding.nextYear.setOnClickListener {
            displayYear = displayYear.plusYears(1)
            binding.currentYear.text = displayYear.value.toString()
            setupCalendar(binding.calendarView)
        }

        /*
         * Set Calendar Days
         */
        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay
            val textView = view.findViewById<TextView>(R.id.calendarDayText)

            init {
                textView.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (selectedDate == null) {
                            selectedDate = day.date
                        }
                        if (selectedDate == day.date) {
                            showMenu(textView)
                        }
                    }
                }
            }
        }

        binding.calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()

                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.visibility = View.VISIBLE
                    when (day.date) {
                        selectedDate -> {
                            textView.setTextColor(ContextCompat.getColor(context!!, R.color.ink))
                            textView.setBackgroundResource(R.drawable.day_selected_background)
                        }
                        today -> {
                            textView.setTextColor(ContextCompat.getColor(context!!, R.color.ink))
                            textView.setBackgroundResource(R.drawable.day_selected_background)
                        }
                        else -> {
                            textView.setTextColor(ContextCompat.getColor(context!!, R.color.ink))
                            textView.setBackgroundResource(R.drawable.day_background)
                        }
                    }

                    val dayOfWeek = day.date.dayOfWeek
                    if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                        textView.setTextColor(ContextCompat.getColor(context!!, R.color.red))
                        textView.setBackgroundResource(R.drawable.day_weekend_background)
                    }

                } else {
                    textView.visibility = View.INVISIBLE
                }
            }
        }

        /*
         * Set Calendar Months
         */
        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = view.findViewById<TextView>(R.id.calendarHeaderText)
        }
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

    private fun setupCalendar(calendar: CalendarView) {
        calendar.setup(
            YearMonth.of(displayYear.value, 1),
            YearMonth.of(displayYear.value, 12),
            DayOfWeek.MONDAY
        )
        if (displayYear.value == YearMonth.now().year) {
            calendar.scrollToMonth(YearMonth.now())
        }
    }

    private fun showMenu(v: View) {
        val popup = PopupMenu(context, v)

        emojisList.forEach {
            popup.menu.add(EmojiCompat.get().process(getEmoji(it)))
        }

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            (v as TextView).text = menuItem.title
            true
        }

        popup.setOnDismissListener {
            // Respond to popup being dismissed.
        }

        // Show the popup menu.
        popup.show()
    }

    fun getEmoji(unicode: Int): String {
        return String(Character.toChars(unicode))
    }
}