package com.practicum.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.ui.State.Value
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.search.domain.api.TrackHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.SearchTracksUseCase
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.search.creator.SearchCreator


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    private val tracksAdapter by lazy {
        TrackAdapter({ track ->
            if (clickTrackDebounce()) {
                historyInteractor.addTrack(track)

                startActivity(
                    Intent(this, PlayerActivity::class.java).putExtra(
                        PlayerActivity.TRACK,
                        gson.toJson(track)
                    )
                )
            }

        })
    }

    private val historyInteractor: TrackHistoryInteractor by lazy {
        SearchCreator.getHistoryInteractor(object : TrackHistoryInteractor.ChangeListener {
            override fun onChange(tracks: List<Track>) {
                if (isHistoryVisibile) {
                    onChange(State.History(tracks))
                }
            }

            override fun onClear() {
                onChange(State.Empty)
            }
        })
    }
    private val searchTracksUseCase by lazy { SearchCreator.provideSearchTracksUseCase() }

    private var stateStateData = State.Empty
    private val gson = Gson()

    private val handler = Handler(Looper.getMainLooper())

    private val searchRunnable by lazy {
        Runnable {
            val searchText = binding.searchText.text.toString()
            if (searchText.isEmpty()) {
                onChange(getHistoryOrEmptyState())
                return@Runnable
            }

            onChange(State.InProgress)
            searchTracksUseCase.search(binding.searchText.text.toString(), object :
                SearchTracksUseCase.TracksConsumer {
                override fun consume(foundTracks: List<Track>) {
                    handler.post {
                        if (foundTracks.isEmpty()) {
                            onChange(State.NotFound)
                        } else {
                            onChange(State.Found(foundTracks))
                        }
                    }
                }

                override fun fail(e: java.lang.Exception) {
                    handler.post {
                        onChange(State.Error)
                    }
                }
            })
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun onChange(state: State) {
        isHistoryVisibile = (state.data.value == Value.History)
        tracksAdapter.updateItems(state.data.foundTracks)
        when (state.data.value) {
            Value.Empty -> {
                binding.updateTracksButton.isVisible = false
                binding.placeholderGroup.isVisible = false
                binding.tracksSearchProgress.isVisible = false
            }

            Value.History -> {
                tracksAdapter.updateItems(state.data.foundTracks)
                binding.updateTracksButton.isVisible = false
                binding.placeholderGroup.isVisible = false
                binding.tracksSearchProgress.isVisible = false
            }

            Value.Error -> {
                binding.noTracksImage.setImageResource(R.drawable.img_no_internet_no_tracks)
                binding.noTracksTextview.setText(resources.getString(R.string.no_internet_no_tracks))
                binding.updateTracksButton.isVisible = true
                binding.placeholderGroup.isVisible = true
                binding.tracksSearchProgress.isVisible = false
            }

            Value.NotFound -> {
                binding.noTracksImage.setImageResource(R.drawable.img_tracks_not_found)
                binding.noTracksTextview.setText(resources.getString(R.string.tracks_not_found))
                binding.updateTracksButton.isVisible = false
                binding.placeholderGroup.isVisible = true
                binding.tracksSearchProgress.isVisible = false
            }

            Value.InProgress -> {
                binding.updateTracksButton.isVisible = false
                binding.placeholderGroup.isVisible = false
                binding.tracksSearchProgress.isVisible = true
            }

            else -> {
                binding.updateTracksButton.isVisible = false
                binding.placeholderGroup.isVisible = false
                binding.tracksSearchProgress.isVisible = false
            }
        }

        stateStateData = state
    }

    private var isHistoryVisibile: Boolean
        set(value) {
            binding.youLookingForText.isVisible = value
            binding.clearHistoryButton.isVisible = value
        }
        get() = binding.searchText.text.isEmpty() && !historyInteractor.isEmpty()

    private fun getHistoryOrEmptyState(): State {
        val tracks = historyInteractor.getTracks()
        return if (tracks.isEmpty())
            State.Empty
        else
            State.History(tracks)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        savedInstanceState?.getString(USER_SEARCH_REQUEST)?.let { value ->
            binding.searchText.setText(value)
        }

        binding.recyclerView.adapter = tracksAdapter

        savedInstanceState?.getString(USER_UI_STATE)?.let {
            val data = gson.fromJson<State.Data>(it, object : TypeToken<State.Data>() {}.type)
            val stateState = State(data)
            onChange(stateState)
        } ?: run {
            onChange(getHistoryOrEmptyState())
        }

        binding.clearIcon.apply {
            setOnClickListener {
                binding.searchText.text.clear()
                onChange(getHistoryOrEmptyState())
                hideKeyboard()
            }
        }.also { clearIcon ->
            binding.searchText.addTextChangedListener { text ->
                clearIcon.isVisible = !text.isNullOrEmpty()
            }
        }

        binding.searchText.doAfterTextChanged { text ->
            searchDebounce()
        }

        binding.searchText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handler.post(searchRunnable)
                true
            } else {
                false
            }
        }

        binding.searchText.setOnFocusChangeListener { view, hasFocus ->
            onChange(getHistoryOrEmptyState())
        }

        binding.updateTracksButton.setOnClickListener {
            handler.post(searchRunnable)
        }

        binding.clearHistoryButton.setOnClickListener {
            historyInteractor.clear()
            onChange(State.Empty)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(searchRunnable)
    }

    companion object {
        private const val USER_UI_STATE = "USER_UI_STATE"
        private const val USER_SEARCH_REQUEST = "USER_SEARCH_REQUEST"

        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_TRACK_DEBOUNCE_DELAY = 1000L
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val json = gson.toJson(stateStateData.data)
        outState.putString(USER_UI_STATE, json)
        outState.putString(USER_SEARCH_REQUEST, binding.searchText.text.toString())
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