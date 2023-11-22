package org.sabaini.moodtracker.presentation.screens.statistics

import androidx.recyclerview.widget.RecyclerView
import org.sabaini.moodtracker.R
import org.sabaini.moodtracker.databinding.StatisticItemBinding
import org.sabaini.moodtracker.domain.model.Statistics
import kotlin.math.roundToInt

class StatisticsViewHolder(private var binding: StatisticItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(statistic: Statistics) {
        binding.apply {
            moodStatistic.text = statistic.mood
            quantityStatistic.progress = statistic.percent?.toInt() ?: 0
            quantityText.text = itemView.context.getString(
                R.string.statistic_value,
                statistic.quantity ?: 0,
                statistic.percent?.roundToInt() ?: 0,
            )
        }
    }
}
