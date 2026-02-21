package com.practicum.playlistmaker.search.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_NO
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.ui.PlayerFragment
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.ui.compose.FailState
import com.practicum.playlistmaker.ui.compose.TrackItem
import com.practicum.playlistmaker.ui.debounce
import com.practicum.playlistmaker.ui.theme.EditorTextColor
import com.practicum.playlistmaker.ui.theme.SearchCursorColor
import com.practicum.playlistmaker.ui.theme.YsTheme
import com.practicum.playlistmaker.ui.theme.getEditorBackgroundColor
import com.practicum.playlistmaker.ui.theme.getEditorIconColor
import com.practicum.playlistmaker.ui.theme.getInactiveColor
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private val viewModel: SearchViewModel by viewModel()

    private val trackClickDebounce =
        debounce<Track>(CLICK_TRACK_DEBOUNCE_DELAY, lifecycleScope, true) { track ->
            viewModel.openTrack(track)
            findNavController().navigate(
                R.id.action_searchFragment_to_playerFragment,
                PlayerFragment.createArgs(track)
            )
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // Автоматически управляет жизненным циклом композиции
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                YsTheme {
                    Scaffold(
                        containerColor = MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = MaterialTheme.colorScheme.surface)
                    ) { innerPadding ->
                        SearchScreen(
                            Modifier
                                .padding(innerPadding), viewModel
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun SearchScreen(modifier: Modifier, searchViewModel: SearchViewModel) {
        val searchText by searchViewModel.searchText.collectAsState()
        val searchState by searchViewModel.getSearchStateFlow().collectAsState()
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            SearchTextField(
                searchText,
                onTextChanged = { newText ->
                    viewModel.searchDebounce(newText)
                },
                onFocus = { viewModel.editSearchRequestFocused() }
            )

            when (searchState) {
                SearchState.Empty -> {
                    Log.d("SearchState.Empty", LOG_TAG)
                }

                is SearchState.Error -> {
                    Log.d("SearchState.Error", LOG_TAG)
                    NoInternetState(
                        onRetrySearch = { viewModel.searchDebounce(viewModel.searchText.value) }
                    )
                }

                is SearchState.Found -> {
                    val tracks = (searchState as SearchState.Found).foundTracks
                    Log.d("SearchState.Found tracks count ${tracks.count()}", LOG_TAG)
                    FoundTracksState(
                        Modifier,
                        tracks,
                        onItemClick = { track -> trackClickDebounce(track) })
                }

                is SearchState.History -> {
                    val tracks = (searchState as SearchState.History).historyTracks
                    Log.d("SearchState.History tracks count ${tracks.count()}", LOG_TAG)
                    HistoryState(
                        Modifier,
                        tracks,
                        onClearHistory = { viewModel.clearHistory() },
                        onItemClick = { track -> trackClickDebounce(track) })
                }

                SearchState.InProgress -> {
                    Log.d("SearchState.InProgress", LOG_TAG)
                    InProgressState()
                }
            }
        }
    }

    @Composable
    fun NoInternetState(onRetrySearch: () -> Unit) {
        FailState(
            imageVector = ImageVector.vectorResource(R.drawable.img_no_internet_no_tracks),
            text = getString(R.string.no_internet_no_tracks),
            onRetrySearch,
            buttonText = getString(R.string.update_tracks)
        )

    }

    @Composable
    fun NoTracksState() {
        FailState(
            imageVector = ImageVector.vectorResource(R.drawable.img_tracks_not_found),
            text = getString(R.string.tracks_not_found),
            {}, ""
        )
    }

    @Composable
    fun SearchTextField(
        text: String,
        onTextChanged: (String) -> Unit,
        onFocus: () -> Unit,
    ) {
        val darkTheme = isSystemInDarkTheme()

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { state ->
                    if (state.isFocused)
                        onFocus()
                },
            value = text,
            textStyle = MaterialTheme.typography.bodyMedium,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = getEditorBackgroundColor(darkTheme),
                focusedTextColor = EditorTextColor,
                unfocusedContainerColor = getEditorBackgroundColor(darkTheme),
                unfocusedTextColor = EditorTextColor,
                cursorColor = SearchCursorColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorPlaceholderColor = getInactiveColor(darkTheme)
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            onValueChange = {
                onTextChanged(it)
            },
            placeholder = {
                Text(
                    getString(R.string.search_text),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            leadingIcon = {
                IconButton(onClick = { onTextChanged("") }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_search_14),
                        tint = getEditorIconColor(darkTheme = isSystemInDarkTheme()),
                        contentDescription = null
                    )
                }
            },
            trailingIcon = {
                if (text.isNotEmpty()) {
                    IconButton(onClick = { onTextChanged("") }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_clear_search),
                            tint = getEditorIconColor(darkTheme = isSystemInDarkTheme()),
                            contentDescription = null
                        )
                    }
                }
            }
        )
    }

    @Composable
    fun InProgressState() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary // @color/search_cursor
            )
        }
    }

    @Composable
    fun FoundTracksState(
        modifier: Modifier,
        tracks: List<Track>,
        onItemClick: (Track) -> Unit,
    ) {

        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            if (tracks.isEmpty())
                NoTracksState()
            else {
                LazyColumn(Modifier.fillMaxSize()) {
                    items(tracks) { track ->
                        TrackItem(
                            track.artworkUrl100,
                            track.trackName,
                            track.artistName,
                            track.lengthText,
                            modifier = Modifier.clickable {
                                onItemClick(track)
                            }
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun HistoryState(
        modifier: Modifier,
        tracks: List<Track>,
        onClearHistory: () -> Unit,
        onItemClick: (Track) -> Unit,
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            if (tracks.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.you_looking_for_text),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 48.dp),
                )
            }

            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                items(tracks) { track ->
                    TrackItem(
                        track.artworkUrl100,
                        track.trackName,
                        track.artistName,
                        track.lengthText,
                        modifier = Modifier.clickable {
                            onItemClick(track)
                        }
                    )
                }
            }

            if (tracks.isNotEmpty()) {
                Button(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onBackground,
                        contentColor = MaterialTheme.colorScheme.surface,
                    ),
                    onClick = { onClearHistory() }
                ) {
                    Text(text = getString(R.string.clear_history))
                }
            }
        }
    }

    fun getTestTracks(): List<Track> = listOf(
        Track(
            trackId = 1,
            trackName = "Name 1",
            artistName = "Artist 1",
            trackTimeMillis = 12345,
            lengthText = "00:04",
            artworkUrl100 = "",
            coverArtwork = "",
            collectionName = "Collection 1",
            trackYear = "1980",
            primaryGenreName = "Rock",
            country = "Australia",
            previewUrl = ""
        ),
        Track(
            trackId = 2,
            trackName = "Name 2",
            artistName = "Artist 2",
            trackTimeMillis = 12345,
            lengthText = "10:04",
            artworkUrl100 = "",
            coverArtwork = "",
            collectionName = "Collection 2",
            trackYear = "1930",
            primaryGenreName = "Rock",
            country = "Australia",
            previewUrl = ""
        )
    )

    @Preview(uiMode = UI_MODE_NIGHT_NO, showBackground = true)
    @Composable
    fun LightModeText() {
        YsTheme {
            SearchTextField("none", {}, {})
        }
    }

    @Preview(uiMode = UI_MODE_NIGHT_YES, showBackground = true)
    @Composable
    fun NightModeText() {
        YsTheme {
            SearchTextField("pink", {}, {})
        }
    }

    @Preview(uiMode = UI_MODE_NIGHT_NO)
    @Composable
    fun SearchInProgress() {
        YsTheme {
            InProgressState()
        }
    }

    @Preview(uiMode = UI_MODE_NIGHT_YES, showBackground = true)
    @Composable
    fun FoundTracksPreview() {
        YsTheme {
            FoundTracksState(Modifier, getTestTracks(), onItemClick = {})
        }
    }

    companion object {
        private const val CLICK_TRACK_DEBOUNCE_DELAY = 1000L
        private const val LOG_TAG = "SearchFragment"
    }
}