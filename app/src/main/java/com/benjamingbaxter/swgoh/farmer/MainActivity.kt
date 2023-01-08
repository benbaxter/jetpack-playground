package com.benjamingbaxter.swgoh.farmer

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.lifecycle.ViewModelProvider
import com.benjamingbaxter.swgoh.farmer.data.UserPreferencesRepository
import com.benjamingbaxter.swgoh.farmer.data.UserPreferencesSerializer
import com.benjamingbaxter.swgoh.farmer.datastore.UserPreferences
import com.benjamingbaxter.swgoh.farmer.ui.theme.StarWarsGOHFarmerTheme
import timber.log.Timber

private const val DATA_STORE_FILE_NAME = "user_prefs.pb"

private val Context.userPreferencesStore: DataStore<UserPreferences> by dataStore(
  fileName = DATA_STORE_FILE_NAME,
  serializer = UserPreferencesSerializer
)

// There is a lot of sample and codelab code in here as I am still learning
// about these components which are new to me.

class MainActivity : ComponentActivity() {

  private lateinit var viewModel: UserPreferenceViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      StarWarsGOHFarmerTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          UserPreferencesPage(viewModel)
        }
      }
    }

    viewModel = ViewModelProvider(
      this,
      CharactersViewModelFactory(
        UserPreferencesRepository(userPreferencesStore)
      )
    ).get(UserPreferenceViewModel::class.java)


    viewModel.initialSetupEvent.observe(this) { initialSetupEvent ->
      Timber.asTree().i("Initial event: %s", initialSetupEvent)

      viewModel.userPreferencesModel.observe(this) { userPreferencesModel ->
        Timber.asTree().i("New event: %s", userPreferencesModel)
        // adapter.submitList(tasksUiModel.tasks)
      }
    }

    // val charactersCall = SwgohApi.swgohService.getCharacters()
    // Timber.asTree().i("Calling Api");
    // charactersCall.enqueue(object : Callback<List<Character>> {
    //   override fun onResponse(call: Call<List<Character>>, response: Response<List<Character>>) {
    //     Timber.asTree().i("Got a response back!");
    //     Timber.asTree().i("characters: %s", response.body()?.size);
    //     Timber.asTree().i("response: %s", response.body());
    //   }
    //
    //   override fun onFailure(call: Call<List<Character>>, t: Throwable) {
    //     Timber.asTree().e(t, "Failed to call swgoh");
    //   }
    // })
  }
}
