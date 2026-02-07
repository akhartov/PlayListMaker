package com.practicum.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_NO
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.library.presentation.LibraryViewModel
import com.practicum.playlistmaker.player.ui.PlayerFragment
import com.practicum.playlistmaker.player.ui.PlayerFragment.Companion.CLICK_DEBOUNCE_DELAY
import com.practicum.playlistmaker.playlist.domain.model.PlaylistCover
import com.practicum.playlistmaker.playlist.ui.view.PlaylistViewerFragment
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.ui.compose.AlbumItem
import com.practicum.playlistmaker.ui.compose.FailState
import com.practicum.playlistmaker.ui.compose.TrackItem
import com.practicum.playlistmaker.ui.debounce
import com.practicum.playlistmaker.ui.theme.YsTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class LibraryFragment : Fragment() {
    private val trackClickDebounce =
        debounce<Track>(CLICK_TRACK_DEBOUNCE_DELAY, lifecycleScope, true) { track ->
            findNavController().navigate(
                R.id.action_libraryFragment_to_playerFragment,
                PlayerFragment.createArgs(track)
            )
        }

    private val albumClickDebounce =
        debounce(CLICK_DEBOUNCE_DELAY, lifecycleScope, true) {
            findNavController().navigate(R.id.action_libraryFragment_to_playlistMakerFragment)
        }

    private val viewModel: LibraryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                YsTheme {
                    LibraryPage(Modifier)
                }
            }
        }
    }

    @Preview(
        uiMode = UI_MODE_NIGHT_NO,
        showBackground = true,
        name = "Light Mode"
    )
    @Composable
    fun LibraryPagePreview() {
        LibraryPage(Modifier)
    }

    @Composable
    fun LibraryPage(modifier: Modifier) {
        val scope = rememberCoroutineScope()
        val tabNames = remember {
            listOf(
                getString(R.string.favourites_tracks),
                getString(R.string.playlists)
            )
        }
        val pagerState = rememberPagerState(pageCount = { tabNames.size })
        val selectedTabIndex = remember { derivedStateOf { pagerState.currentPage } }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            SecondaryTabRow(
                selectedTabIndex = selectedTabIndex.value,
                modifier = Modifier.fillMaxWidth(),
                divider = {},
                indicator = {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(selectedTabIndex.value),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            ) {
                tabNames.forEachIndexed { index, tabName ->
                    Tab(
                        selected = selectedTabIndex.value == index,
                        selectedContentColor = MaterialTheme.colorScheme.onBackground,
                        unselectedContentColor = MaterialTheme.colorScheme.onBackground,
                        onClick = {
                            scope.launch {
                                pagerState.requestScrollToPage(index)
                            }
                        },
                        text = { Text(text = tabName, style = MaterialTheme.typography.bodySmall) },
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    when (selectedTabIndex.value) {
                        0 -> FavouritesTab()
                        1 -> PlaylistsTab()
                    }
                }
            }
        }
    }

    @Composable
    fun FavouritesTab() {
        val tracks by viewModel.getTracksStateFlow().collectAsState()
        if (tracks.isEmpty())
            NoTracksState()
        else
            TracksState(tracks)
    }

    @Preview
    @Composable
    fun PlaylistsTabPreview() {
        PlaylistsTab()
    }

    @Composable
    fun PlaylistsTab() {
        val albums by viewModel.getAlbumsStateFlow().collectAsState()
        if (albums.items.isEmpty())
            NoAlbumsState()
        else
            AlbumsGrid(albums.items)
    }

    @Composable
    private fun NoTracksState() {
        FailState(
            imageVector = ImageVector.vectorResource(R.drawable.img_tracks_not_found),
            text = getString(R.string.history_tracks_is_empty)
        )
    }

    @Composable
    private fun TracksState(tracks: List<Track>) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
        ) {
            items(tracks) { track ->
                TrackItem(
                    track.artworkUrl100,
                    track.trackName,
                    track.artistName,
                    track.lengthText,
                    modifier = Modifier.clickable {
                        trackClickDebounce(track)
                    }
                )
            }
        }
    }

    @Composable
    private fun NoAlbumsState() {
        FailState(
            imageVector = ImageVector.vectorResource(R.drawable.img_tracks_not_found),
            text = getString(R.string.no_playlists_text)
        )
    }

    @Composable
    fun AlbumsGrid(albums: List<PlaylistCover>) {
        val newPlaylistText = remember { getString(R.string.new_playlist) }

        Column(modifier = Modifier.fillMaxSize()) {
            Button(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 24.dp, bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    contentColor = MaterialTheme.colorScheme.surface,
                ),
                onClick = { albumClickDebounce() }
            ) {
                Text(text = newPlaylistText)
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                content = {
                    items(albums) { item ->
                        AlbumItem(
                            item.imagePath, item.title, item.tracksCountText,
                            modifier = Modifier.clickable {
                                navigateToAlbum(item.id)
                            })
                    }
                }
            )
        }
    }

    private fun navigateToAlbum(playlistId: Int) {
        findNavController().navigate(
            R.id.action_libraryFragment_to_playlistViewerFragment,
            PlaylistViewerFragment.createArgs(playlistId)
        )
    }

    companion object {
        private const val CLICK_TRACK_DEBOUNCE_DELAY = 1000L
    }
}