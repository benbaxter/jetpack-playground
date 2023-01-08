package com.benjamingbaxter.swgoh.farmer.data

import androidx.datastore.core.DataStore
import com.benjamingbaxter.swgoh.farmer.datastore.UserPreferences
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import timber.log.Timber

class UserPreferencesRepository(
  private val userPreferencesStore: DataStore<UserPreferences>
) {

  val userPreferencesFlow: Flow<UserPreferences> = userPreferencesStore.data
    .catch { exception ->
      // dataStore.data throws an IOException when an error is encountered when reading data
      if (exception is IOException) {
        Timber.asTree().e(exception, "Error reading user preferences.")
        emit(UserPreferences.getDefaultInstance())
      } else {
        throw exception
      }
    }

  suspend fun fetchInitialPreferences() = userPreferencesStore.data.first()

  suspend fun updatePlayerId(playerId: String) {
    Timber.asTree().d("Saving %s", playerId)
    userPreferencesStore.updateData { preferences ->
      preferences.toBuilder().setPlayerId(playerId).build()
    }
  }

  suspend fun updateShowCompleted(completed: Boolean) {
    userPreferencesStore.updateData { preferences ->
      preferences.toBuilder().setShowCompleted(completed).build()
    }
  }
}