package org.sabaini.moodtracker.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
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
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*

class CalendarFragment : Fragment() {

    private val viewModel: CalendarViewModel by lazy {
        ViewModelProvider(this).get(CalendarViewModel::class.java)
    }

    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCalendarBinding.inflate(layoutInflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        val firstDayOfWeek = DayOfWeek.MONDAY

        binding.calendarView.setup(
            YearMonth.now().minusMonths(3),
            YearMonth.now().plusMonths(10),
            firstDayOfWeek
        )
        binding.calendarView.scrollToMonth(YearMonth.now())

        /*
         * Set Calendar Days
         */
        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay
            val textView = view.findViewById<TextView>(R.id.calendarDayText)

            init {
                textView.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (selectedDate == day.date) {
                            selectedDate = null
                            binding.calendarView.notifyDayChanged(day)
                        } else {
                            val oldDate = selectedDate
                            selectedDate = day.date
                            binding.calendarView.notifyDateChanged(day.date)
                            oldDate?.let { binding.calendarView.notifyDateChanged(oldDate) }
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
                            if (day.date == today) {
                                Log.d("teste", "Today!")
                            }
                        }
                        today -> {
                            textView.setTextColor(ContextCompat.getColor(context!!,R.color.ink))
                            textView.setBackgroundResource(R.drawable.day_selected_background)
                        }
                        else -> {
                            textView.setTextColor(ContextCompat.getColor(context!!,R.color.ink))
                            textView.setBackgroundResource(R.drawable.day_background)
                        }
                    }

                    val dayOfWeek = day.date.dayOfWeek
                    if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                        textView.setTextColor(ContextCompat.getColor(context!!,R.color.red))
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
}