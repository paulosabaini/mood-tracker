package org.sabaini.moodtracker.presentation.screens.calendar

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.emoji.text.EmojiCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import dagger.hilt.android.AndroidEntryPoint
import org.sabaini.moodtracker.R
import org.sabaini.moodtracker.databinding.FragmentCalendarBinding
import java.time.DayOfWeek
import java.time.Year
import java.time.YearMonth

@AndroidEntryPoint
class CalendarFragment : Fragment() {

    companion object {
        private const val START_MONTH = 1
        private const val END_MONTH = 12
    }

    private val viewModel: CalendarViewModel by viewModels()
    private lateinit var binding: FragmentCalendarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCalendarBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.displayYear.observe(viewLifecycleOwner, ::onDisplayYear)
        viewModel.moods.observe(viewLifecycleOwner) {
            binding.calendarView.notifyCalendarChanged()
        }

        setupListeners()
        setupDayBinder()
        setupMonthHeaderBinder()

        return binding.root
    }

    private fun setupMonthHeaderBinder() {
        binding.calendarView.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View) = MonthViewContainer(view)

                override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                    container.textView.text = viewModel.getMonthName(data)
                }
            }
    }

    private fun setupDayBinder() {
        binding.calendarView.dayBinder =
            object : MonthDayBinder<DayViewContainer> {
                override fun create(view: View) = DayViewContainer(view)

                override fun bind(container: DayViewContainer, data: CalendarDay) {
                    val textView = container.textView
                    val dayText = viewModel.getDayText(data.date)
                    textView.text = dayText.first
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, dayText.second)

                    container.onClick = {
                        if (viewModel.shouldDisplayEmojiPicker(data)) {
                            showMenu(textView)
                        }
                    }

                    setDayStyle(data, textView)
                }
            }
    }

    private fun setupListeners() {
        binding.previousYear.setOnClickListener {
            viewModel.decrementDisplayYear()
        }

        binding.nextYear.setOnClickListener {
            viewModel.incrementDisplayYear()
        }
    }

    private fun onDisplayYear(year: Year) {
        binding.calendarView.setup(
            startMonth = YearMonth.of(year.value, START_MONTH),
            endMonth = YearMonth.of(year.value, END_MONTH),
            firstDayOfWeek = DayOfWeek.SUNDAY,
        )

        if (viewModel.isCurrentYear(year)) {
            binding.calendarView.scrollToMonth(YearMonth.now())
        }
    }

    private fun setDayStyle(data: CalendarDay, textView: TextView) {
        if (data.position == DayPosition.MonthDate) {
            textView.visibility = View.VISIBLE
            textView.setTextColor(
                if (viewModel.isWeekend(data)) {
                    ContextCompat.getColor(requireContext(), R.color.red)
                } else {
                    ContextCompat.getColor(requireContext(), R.color.ink)
                },
            )
            textView.setBackgroundResource(
                if (viewModel.isToday(data)) {
                    R.drawable.day_selected_background
                } else if (viewModel.isWeekend(data)) {
                    R.drawable.day_weekend_background
                } else {
                    R.drawable.day_background
                },
            )
        } else {
            textView.visibility = View.INVISIBLE
        }
    }

    private fun showMenu(view: View) {
        val popup = PopupMenu(context, view)

        viewModel.emojiList.value!!.forEach {
            val emoji = SpannableString(EmojiCompat.get().process(it))
            emoji.setSpan(
                AbsoluteSizeSpan(32, true),
                0,
                emoji.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
            popup.menu.add(emoji)
        }

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            (view as TextView).text = menuItem.title
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32f)
            viewModel.saveMood(menuItem.title.toString())
            true
        }

        // Show the popup menu.
        popup.show()
    }
}
