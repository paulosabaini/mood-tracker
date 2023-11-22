package org.sabaini.moodtracker.presentation.screens.statistics

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.sabaini.moodtracker.R.color.gray
import org.sabaini.moodtracker.R.color.ink
import org.sabaini.moodtracker.databinding.FragmentStatisticsBinding
import org.sabaini.moodtracker.domain.model.Statistics

@AndroidEntryPoint
class StatisticsFragment : Fragment() {

    private val viewModel: StatisticsViewModel by viewModels()
    private lateinit var binding: FragmentStatisticsBinding
    private val adapter = StatisticsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentStatisticsBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.moodsStats.adapter = adapter
        binding.moodsStats.layoutManager = LinearLayoutManager(requireContext())

        viewModel.statistics.observe(viewLifecycleOwner, ::onStatistics)
        viewModel.filter.observe(viewLifecycleOwner, ::onFilter)

        return binding.root
    }

    private fun onStatistics(statistics: List<Statistics>) {
        adapter.statistics = statistics
    }

    private fun onFilter(type: StatisticFilterType) {
        val selectedColor = ContextCompat.getColor(requireContext(), ink)
        val unselectedColor = ContextCompat.getColor(requireContext(), gray)

        val buttons = mapOf(
            binding.allStats to StatisticFilterType.ALL,
            binding.monthStats to StatisticFilterType.MONTH,
            binding.weekStats to StatisticFilterType.WEEK,
            binding.yearStats to StatisticFilterType.YEAR,
        )

        for ((button, filterType) in buttons) {
            button.setTextColor(if (type == filterType) selectedColor else unselectedColor)
            button.typeface = if (type == filterType) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
        }
    }
}
