package com.practicum.playlistmaker.search.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySearchBinding


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel
    private var textWatcher: TextWatcher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = createViewModel()

        binding.backButton.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.updateTracksButton.setOnClickListener {
            viewModel.searchDebounce(binding.searchText.text.toString())
        }

        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
        }

        binding.recyclerView.adapter = tracksAdapter
        prepareTextEditor()
    }

    private val tracksAdapter by lazy {
        TrackAdapter({ track ->
            if (clickTrackDebounce()) {
                viewModel.openTrack(track)
            }
        })
    }

    private val handler = Handler(Looper.getMainLooper())

    fun updateHistoryControlsVisibility(visible: Boolean) {
        binding.youLookingForText.isVisible = visible
        binding.clearHistoryButton.isVisible = visible
    }

    private fun createViewModel(): SearchViewModel {
        return ViewModelProvider(this, SearchViewModel.getFactory())
            .get(SearchViewModel::class.java)
            .apply {
                getSearchStateLiveData().observe(this@SearchActivity) { searchState ->
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
            if(hasFocus)
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

    companion object {
        private const val CLICK_TRACK_DEBOUNCE_DELAY = 1000L
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        inputMethodManager?.hideSoftInputFromWindow(binding.searchText.windowToken, 0)
    }

    private var isClickTrackAllowed = true

    private fun clickTrackDebounce(): Boolean {
        val current = isClickTrackAllowed
        if (isClickTrackAllowed) {
            isClickTrackAllowed = false
            handler.postDelayed({ isClickTrackAllowed = true }, CLICK_TRACK_DEBOUNCE_DELAY)
        }
        return current
    }
}