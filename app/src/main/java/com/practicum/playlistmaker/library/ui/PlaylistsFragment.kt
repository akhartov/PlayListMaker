package com.practicum.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.ui.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment: BindingFragment<FragmentPlaylistsBinding>() {

    private val playlistsAdapter by lazy {
        PlaylistGridAdapter { playlist ->
            //TODO: impl
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistsBinding {
        return FragmentPlaylistsBinding.inflate(inflater, container, false)
    }

    private val viewModel: PlaylistsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playlistsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

        binding.playlistsRecyclerView.adapter = playlistsAdapter

        binding.newPlaylistButton.setOnClickListener {
            //TODO: impl
        }

        viewModel.getPlaylistLiveData().observe(viewLifecycleOwner) { items ->
            playlistsAdapter.updateItems(items)
        }
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}