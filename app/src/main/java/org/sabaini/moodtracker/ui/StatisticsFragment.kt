package org.sabaini.moodtracker.ui

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.sabaini.moodtracker.R
import org.sabaini.moodtracker.databinding.FragmentStatisticsBinding
import org.sabaini.moodtracker.viewmodel.StatisticsViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.*
import kotlin.math.roundToInt

class StatisticsFragment : Fragment() {

    private val viewModel: StatisticsViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, StatisticsViewModel.Factory(activity.application)).get(
            StatisticsViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentStatisticsBinding.inflate(layoutInflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        viewModel.monthStatistics.observe(viewLifecycleOwner, Observer { statistics ->
            binding.moodsStats.removeAllViews()
            statistics.forEach { statistic ->
                val view = layoutInflater.inflate(R.layout.statistics_layout, null)
                val moodStatistic: TextView = view.findViewById(R.id.mood_statistic)
                val quantityStatistic: ProgressBar = view.findViewById(R.id.quantity_statistic)
                val quantityText: TextView = view.findViewById(R.id.quantity_text)
                moodStatistic.text = statistic.mood
                quantityStatistic.progress = statistic.percent!!.toInt()
                quantityText.text = "${statistic.quantity} (${statistic.percent.roundToInt()}%)"
                binding.moodsStats.addView(view)
            }
        })

        viewModel.filter.observe(viewLifecycleOwner, Observer { view ->
            val now = LocalDate.now()
            when (view.id) {
                R.id.week_stats -> {
                    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
                    val lastDayOfWeek =
                        DayOfWeek.of(((firstDayOfWeek.getValue() + 5) % DayOfWeek.values().size) + 1)

                    viewModel.updateStatistics(
                        now.with(TemporalAdjusters.previousOrSame(firstDayOfWeek)).toEpochDay(),
                        now.with(TemporalAdjusters.nextOrSame(lastDayOfWeek)).toEpochDay()
                    )
                }
                R.id.month_stats -> {
                    viewModel.updateStatistics(
                        now.withDayOfMonth(1).toEpochDay(),
                        now.withDayOfMonth(now.lengthOfMonth()).toEpochDay()
                    )
                }
                R.id.year_stats -> {
                    viewModel.updateStatistics(
                        now.withDayOfYear(1).toEpochDay(),
                        now.withDayOfYear(now.lengthOfYear()).toEpochDay()
                    )
                }
                R.id.all_stats -> {
                    viewModel.updateStatistics(null, null)
                }
            }
            val list = listOf(
                binding.allStats,
                binding.monthStats,
                binding.weekStats,
                binding.yearStats
            )
            setMarked(view, list)
        })

        return binding.root
    }

    private fun setMarked(view: View, list: List<Button>) {
        list.forEach {
            it.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.gray
                )
            )
            it.setTypeface(Typeface.DEFAULT)
        }
        (view as Button).setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.ink
            )
        )
        view.setTypeface(view.typeface, Typeface.BOLD)
    }
}