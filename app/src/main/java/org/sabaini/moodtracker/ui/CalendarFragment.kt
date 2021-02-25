package org.sabaini.moodtracker.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import org.sabaini.moodtracker.R
import org.sabaini.moodtracker.databinding.FragmentCalendarBinding
import org.sabaini.moodtracker.viewmodel.CalendarViewModel

class CalendarFragment : Fragment() {

    private val viewModel: CalendarViewModel by lazy {
        ViewModelProvider(this).get(CalendarViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCalendarBinding.inflate(layoutInflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        return binding.root
    }
}