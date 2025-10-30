package com.practicum.playlistmaker.search.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.player.ui.PlayerFragment
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.ui.BindingFragment
import com.practicum.playlistmaker.ui.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BindingFragment<FragmentSearchBinding>() {
    private val viewModel: SearchViewModel by viewModel()
    private var textWatcher: TextWatcher? = null

    private val tracksAdapter by lazy {
        TrackAdapter { track ->
            tracksClickDebounce(track)
        }
    }

    private val tracksClickDebounce =
        debounce<Track>(CLICK_TRACK_DEBOUNCE_DELAY, lifecycleScope, true) { track ->
            viewModel.openTrack(track)
            findNavController().navigate(
                R.id.action_searchFragment_to_playerFragment,
                PlayerFragment.createArgs(track)
            )
        }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()

        binding.updateTracksButton.setOnClickListener {
            viewModel.searchDebounce(binding.searchText.text.toString())
        }

        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
        }

        binding.recyclerView.adapter = tracksAdapter
        prepareTextEditor()
    }

    private fun updateHistoryControlsVisibility(visible: Boolean) {
        binding.youLookingForText.isVisible = visible
        binding.clearHistoryButton.isVisible = visible
    }


    private fun initViewModel() {
        viewModel.apply {
            getSearchStateLiveData().observe(viewLifecycleOwner) { searchState ->
                updateHistoryControlsVisibility(searchState is SearchState.History)
                when (searchState) {
                    is SearchState.Empty -> {
                        tracksAdapter.updateItems(emptyList())
                        binding.updateTracksButton.isVisible = false
                        binding.placeholderGroup.isVisible = false
                        binding.tracksSearchProgress.isVisible = false
                    }

                    is SearchState.History -> {
                        tracksAdapter.updateItems(searchState.historyTracks)
                        binding.updateTracksButton.isVisible = false
                        binding.placeholderGroup.isVisible = false
                        binding.tracksSearchProgress.isVisible = false
                    }

                    is SearchState.Error -> {
                        tracksAdapter.updateItems(emptyList())
                        binding.noTracksImage.setImageResource(R.drawable.img_no_internet_no_tracks)
                        binding.noTracksTextview.setText(resources.getString(R.string.no_internet_no_tracks))
                        binding.updateTracksButton.isVisible = true
                        binding.placeholderGroup.isVisible = true
                        binding.tracksSearchProgress.isVisible = false
                    }

                    is SearchState.NotFound -> {
                        tracksAdapter.updateItems(emptyList())
                        binding.noTracksImage.setImageResource(R.drawable.img_tracks_not_found)
                        binding.noTracksTextview.setText(resources.getString(R.string.tracks_not_found))
                        binding.updateTracksButton.isVisible = false
                        binding.placeholderGroup.isVisible = true
                        binding.tracksSearchProgress.isVisible = false
                    }

                    is SearchState.InProgress -> {
                        tracksAdapter.updateItems(emptyList())
                        binding.updateTracksButton.isVisible = false
                        binding.placeholderGroup.isVisible = false
                        binding.tracksSearchProgress.isVisible = true
                    }

                    is SearchState.Found -> {
                        tracksAdapter.updateItems(searchState.foundTracks)
                        binding.updateTracksButton.isVisible = false
                        binding.placeholderGroup.isVisible = false
                        binding.tracksSearchProgress.isVisible = false
                    }
                }
            }
        }
    }

    private fun prepareTextEditor() {

        binding.clearTextIcon.apply {
            setOnClickListener {
                binding.searchText.text.clear()
                hideKeyboard()
            }
        }.also { clearIcon ->
            binding.searchText.addTextChangedListener { text ->
                clearIcon.isVisible = !text.isNullOrEmpty()
            }
        }

        binding.searchText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                viewModel.showHistory()
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }
        }
        textWatcher?.let { binding.searchText.addTextChangedListener(it) }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager

        inputMethodManager?.hideSoftInputFromWindow(binding.searchText.windowToken, 0)
    }

    companion object {
        private const val CLICK_TRACK_DEBOUNCE_DELAY = 1000L
    }
}