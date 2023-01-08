package com.benjamingbaxter.swgoh.farmer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.benjamingbaxter.swgoh.farmer.data.UserPreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import timber.log.Timber

class UserPreferenceViewModel(
  private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

  val initialSetupEvent = liveData {
    emit(userPreferencesRepository.fetchInitialPreferences())
  }

  val userPreferencesModel = userPreferencesRepository.userPreferencesFlow.asLiveData()
  val  userPreferenceFlow = userPreferencesRepository.userPreferencesFlow
  val playerIdText = MutableStateFlow("")
  // val playerIdText = userPreferencesRepository.userPreferencesFlow.map { it.playerId }
  val processingPlayerIdInput = MutableStateFlow(false)

  @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
  fun storePlayerId() {
    viewModelScope.launch {
      processingPlayerIdInput.value = true
      playerIdText
        .debounce(250)
        .distinctUntilChanged()
        .collectLatest {
          Timber.asTree().d("Saving %s", it)
          userPreferencesRepository.updatePlayerId(it)
          processingPlayerIdInput.value = false
          userPreferencesRepository.userPreferencesFlow
        }
    }
  }

  fun showCompletedTasks(show: Boolean) {
    viewModelScope.launch {
      userPreferencesRepository.updateShowCompleted(show)
    }
  }
}

class CharactersViewModelFactory(
  private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModelProvider.Factory {

  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(UserPreferenceViewModel::class.java)) {
      @Suppress("UNCHECKED_CAST")
      return UserPreferenceViewModel(userPreferencesRepository) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}