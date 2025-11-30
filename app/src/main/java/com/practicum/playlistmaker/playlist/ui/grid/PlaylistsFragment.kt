package com.practicum.playlistmaker.playlist.ui.grid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.player.ui.PlayerFragment.Companion.CLICK_DEBOUNCE_DELAY
import com.practicum.playlistmaker.playlist.ui.view.PlaylistViewerFragment
import com.practicum.playlistmaker.ui.BindingFragment
import com.practicum.playlistmaker.ui.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : BindingFragment<FragmentPlaylistsBinding>() {
    private val playlistsAdapter by lazy {
        PlaylistGridAdapter { cover ->
            findNavController().navigate(
                R.id.action_libraryFragment_to_playlistViewerFragment,
                PlaylistViewerFragment.createArgs(cover.id)
            )
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistsBinding {
        return FragmentPlaylistsBinding.inflate(inflater, container, false)
    }

    private val viewModel: PlaylistsViewModel by viewModel()

    private val playlistClickDebounce =
        debounce<Int>(CLICK_DEBOUNCE_DELAY, lifecycleScope, true) { _ ->
            findNavController().navigate(R.id.action_libraryFragment_to_playlistMakerFragment)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playlistsRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

        binding.playlistsRecyclerView.adapter = playlistsAdapter

        binding.newPlaylistButton.setOnClickListener {
            playlistClickDebounce(0)
        }

        viewModel.coverLibraryLiveData.observe(viewLifecycleOwner) { state ->
            playlistsAdapter.updateItems(state.items)
            binding.placeholderGroup.isVisible = state.items.isEmpty()
        }
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}