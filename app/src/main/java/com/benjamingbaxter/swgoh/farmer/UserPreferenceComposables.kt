package com.benjamingbaxter.swgoh.farmer

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStoreFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import com.benjamingbaxter.swgoh.farmer.data.UserPreferencesRepository
import com.benjamingbaxter.swgoh.farmer.data.UserPreferencesSerializer
import com.benjamingbaxter.swgoh.farmer.datastore.UserPreferences
import com.benjamingbaxter.swgoh.farmer.ui.theme.StarWarsGOHFarmerTheme
import com.benjamingbaxter.swgoh.farmer.ui.theme.md_theme_dark_primary
import com.benjamingbaxter.swgoh.farmer.ui.theme.md_theme_light_primary
import java.io.File

@Composable
fun UserPreferencesPage(viewModel: UserPreferenceViewModel = viewModel()) {
  Column {
    Text(text = stringResource(id = R.string.user_prefs_title))
    InputPlayerId(viewModel)
  }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun InputPlayerId(viewModel: UserPreferenceViewModel = viewModel()) {
  // var text by remember { mutableStateOf("") }
  val text by viewModel.playerIdText.collectAsState()
  val processingInput by viewModel.processingPlayerIdInput.collectAsState()

  val data = viewModel.userPreferenceFlow.collectAsState(initial = UserPreferences.getDefaultInstance())
  val userPrefs = data.value

  Row(verticalAlignment = Alignment.CenterVertically) {
    Column {
      OutlinedTextField(
        // How do I get this value to be populated with the value stored
        // in the proto data store? Basically, I want something similar
        // to two-way binding. What I have works kinda but I am not sure
        // if this is the "correct" way...
        value = userPrefs.playerId,
        onValueChange = {
          // This seems to be a buggy approach as the debounce logic is impacting
          // user input and the cursor jumps if you type quickly.
          viewModel.playerIdText.value = it
          viewModel.storePlayerId()
        },
        label = { Text(stringResource(id = R.string.user_prefs_player_id)) },
        maxLines = 1,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done))
    }
    Column() {
      AnimatedContent(targetState = processingInput) { processing ->
        if(processing) {
          CircularProgressIndicator(modifier = Modifier.size(24.dp))
        } else {
          Icon(
            painter = painterResource(id = R.drawable.ic_baseline_check_24),
            contentDescription = stringResource(id = R.string.user_prefs_content_description_save_completed),
            tint = if (isSystemInDarkTheme()) {
              md_theme_dark_primary
            } else {
              md_theme_light_primary
            }
          )
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  StarWarsGOHFarmerTheme {
    UserPreferencesPage(UserPreferenceViewModel(UserPreferencesRepository(DataStoreFactory.create(
      serializer = UserPreferencesSerializer,
      produceFile = {  File("user_prefs.pb") }
    ))))
  }
}