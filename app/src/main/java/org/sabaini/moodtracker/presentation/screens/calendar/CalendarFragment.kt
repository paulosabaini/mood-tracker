package org.sabaini.moodtracker.presentation.screens.calendar

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
import java.util.*

@AndroidEntryPoint
class CalendarFragment : Fragment() {

    private val viewModel: CalendarViewModel by viewModels()
    private lateinit var binding: FragmentCalendarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.displayYear.observe(viewLifecycleOwner, ::onDisplayYear)

        binding.previousYear.setOnClickListener {
            viewModel.decrementDisplayYear()
        }

        binding.nextYear.setOnClickListener {
            viewModel.incrementDisplayYear()
        }

        binding.calendarView.dayBinder =
            object : MonthDayBinder<DayViewContainer> {
                // Called only when a new container is needed.
                override fun create(view: View) = DayViewContainer(view)

                // Called every time we need to reuse a container.
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

                    if (data.position == DayPosition.MonthDate) {
                        textView.visibility = View.VISIBLE
                        when (data.date) {
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

                        val dayOfWeek = data.date.dayOfWeek
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

        binding.calendarView.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {

                override fun create(view: View) = MonthViewContainer(view)

                override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                    @SuppressLint("SetTextI18n") // Concatenation warning for `setText` call.
                    container.textView.text =
                        data.yearMonth.month.name.uppercase(Locale.getDefault())
                }
            }

        return binding.root
    }

    private fun onDisplayYear(year: Year) {
        binding.calendarView.setup(
            YearMonth.of(year.value, 1),
            YearMonth.of(year.value, 12),
            DayOfWeek.SUNDAY
        )

        if (year.value == YearMonth.now().year) {
            binding.calendarView.scrollToMonth(YearMonth.now())
        }
    }

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