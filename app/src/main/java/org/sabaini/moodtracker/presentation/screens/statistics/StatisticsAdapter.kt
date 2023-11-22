package org.sabaini.moodtracker.presentation.screens.statistics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sabaini.moodtracker.databinding.StatisticItemBinding
import org.sabaini.moodtracker.domain.model.Statistics

class StatisticsAdapter : RecyclerView.Adapter<StatisticsViewHolder>() {
    var statistics = emptyList<Statistics>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticsViewHolder {
        return StatisticsViewHolder(
            StatisticItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }

    override fun onBindViewHolder(holder: StatisticsViewHolder, position: Int) {
        val statistic = statistics[position]
        holder.bind(statistic)
    }

    override fun getItemCount() = statistics.size
}
