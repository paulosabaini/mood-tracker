package org.sabaini.moodtracker.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import org.sabaini.moodtracker.R
import org.sabaini.moodtracker.databinding.FragmentStatisticsBinding
import org.sabaini.moodtracker.viewmodel.CalendarViewModel
import org.sabaini.moodtracker.viewmodel.StatisticsViewModel

class StatisticsFragment : Fragment() {

    private val viewModel: StatisticsViewModel by lazy {
        ViewModelProvider(this).get(StatisticsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentStatisticsBinding.inflate(layoutInflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        return binding.root
    }
}