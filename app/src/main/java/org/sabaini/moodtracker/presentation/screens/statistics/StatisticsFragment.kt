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

        setupFilterListener()

        viewModel.statistics.observe(viewLifecycleOwner, ::onStatistics)
        viewModel.filter.observe(viewLifecycleOwner, ::onFilter)

        return binding.root
    }

    private fun setupFilterListener() {
        binding.filterGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                val type = when (checkedId) {
                    binding.weekStats.id -> StatisticFilterType.WEEK
                    binding.monthStats.id -> StatisticFilterType.MONTH
                    binding.yearStats.id -> StatisticFilterType.YEAR
                    binding.allStats.id -> StatisticFilterType.ALL
                    else -> StatisticFilterType.ALL
                }
                viewModel.filterClick(type)
            }
        }
    }

    private fun onStatistics(statistics: List<Statistics>) {
        adapter.statistics = statistics
    }

    private fun onFilter(type: StatisticFilterType) {
        val checkedId = when (type) {
            StatisticFilterType.WEEK -> binding.weekStats.id
            StatisticFilterType.MONTH -> binding.monthStats.id
            StatisticFilterType.YEAR -> binding.yearStats.id
            StatisticFilterType.ALL -> binding.allStats.id
        }
        if (binding.filterGroup.checkedButtonId != checkedId) {
            binding.filterGroup.check(checkedId)
        }
    }
}
