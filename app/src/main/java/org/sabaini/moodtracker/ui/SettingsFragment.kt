package org.sabaini.moodtracker.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import org.sabaini.moodtracker.R
import org.sabaini.moodtracker.databinding.FragmentSettingsBinding
import org.sabaini.moodtracker.viewmodel.CalendarViewModel
import org.sabaini.moodtracker.viewmodel.SettingsViewModel

class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by lazy {
        ViewModelProvider(this).get(SettingsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =  FragmentSettingsBinding.inflate(layoutInflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        return binding.root
    }
}