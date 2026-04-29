package org.sabaini.moodtracker.presentation.screens.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sabaini.moodtracker.BuildConfig
import org.sabaini.moodtracker.data.local.prefs.PreferenceManager
import org.sabaini.moodtracker.domain.repository.MoodTrackerRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val repository: MoodTrackerRepository
) : ViewModel() {

    val isDarkMode: LiveData<Boolean> = preferenceManager.isDarkModeEnabled.asLiveData()

    private val _appVersion = MutableLiveData<String>()
    val appVersion: LiveData<String> = _appVersion

    init {
        _appVersion.value = BuildConfig.VERSION_NAME
    }

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            preferenceManager.setDarkModeEnabled(enabled)
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            repository.clearAllData()
        }
    }
}
