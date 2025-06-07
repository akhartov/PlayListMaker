package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.Ui.State


class SearchActivity : AppCompatActivity(), OnTrackClickListener, UiStateListener {
    private val editor by lazy { findViewById<EditText>(R.id.inputEditText) }
    private val tracksAdapter by lazy { TrackAdapter(this) }
    private val trackHistoryAdapter by lazy { TrackAdapter(this) }
    private val noTracksPlaceholder by lazy { findViewById<LinearLayout>(R.id.placeholder_layout) }
    private val noTracksTextView by lazy { findViewById<TextView>(R.id.no_tracks_textview) }
    private val noTracksImageView by lazy { findViewById<ImageView>(R.id.no_tracks_image) }
    private val retrySearchButton by lazy { findViewById<Button>(R.id.update_tracks_button) }
    private val youLookingFor by lazy { findViewById<TextView>(R.id.you_looking_for_text) }
    private val clearHistoryButton by lazy { findViewById<Button>(R.id.clear_history_button) }
    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.recyclerView) }
    private val searchEngine by lazy { SearchEngine(this) }

    private lateinit var history: SearchHistory

    private var uiStateData = Ui.Empty
    private val gson = Gson()

    override fun onChange(state: Ui) {
        if (!state.data.searchRequest.isNullOrEmpty() && state.data.searchRequest != editor.text.toString())
            editor.setText(state.data.searchRequest)

        val newAdapter = if (state.data.state == State.History) {
            trackHistoryAdapter
        } else tracksAdapter

        if (newAdapter != recyclerView.adapter)
            recyclerView.adapter = newAdapter

        if(newAdapter.tracks != state.data.foundTracks)
            newAdapter.updateItems(state.data.foundTracks)


        isHistoryVisibile = (state.data.state == State.History)

        when (state.data.state) {
            State.History -> {
                retrySearchButton.isVisible = false
                noTracksPlaceholder.isVisible = false
            }

            State.Error -> {
                noTracksImageView.setImageResource(R.drawable.img_no_internet_no_tracks)
                noTracksTextView.setText(resources.getString(R.string.no_internet_no_tracks))
                retrySearchButton.isVisible = true
                noTracksPlaceholder.isVisible = true
            }

            State.NotFound -> {
                noTracksImageView.setImageResource(R.drawable.img_tracks_not_found)
                noTracksTextView.setText(resources.getString(R.string.tracks_not_found))
                retrySearchButton.isVisible = false
                noTracksPlaceholder.isVisible = true
            }

            State.InProgress -> {
                retrySearchButton.isVisible = false
                noTracksPlaceholder.isVisible = false
            }

            else -> {
                noTracksPlaceholder.isVisible = false
            }
        }

        uiStateData = state
    }

    private var isHistoryVisibile: Boolean
        set(value) {
            youLookingFor.isVisible = value
            clearHistoryButton.isVisible = value
        }
        get() = editor.isFocused() && editor.text.isEmpty() && !trackHistoryAdapter.tracks.isEmpty()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        findViewById<MaterialToolbar>(R.id.back).setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        recyclerView.adapter = tracksAdapter

        history = SearchHistory(getSharedPreferences("SEARCH", MODE_PRIVATE), trackHistoryAdapter)

        savedInstanceState?.getString(USER_SEARCH_STATE)?.let {
            val data = gson.fromJson<Ui.Data>(it, object : TypeToken<Ui.Data>() {}.type)
            val uiState = Ui(data)
            onChange(uiState)
        }

        findViewById<ImageView>(R.id.clearIcon).apply {
            setOnClickListener {
                editor.setText("")
                onChange(Ui.History(history.getTracks()))
                hideKeyboard()
            }
        }.also { clearIcon ->
            editor.addTextChangedListener { text ->
                clearIcon.isVisible = !text.isNullOrEmpty()
            }
        }

        editor.doAfterTextChanged { text ->
            if (isHistoryVisibile) {
                onChange(Ui.History(trackHistoryAdapter.tracks))
            } else {
                onChange(Ui.Found(text.toString(), tracksAdapter.tracks))
            }
        }

        editor.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchEngine.search(v.text.toString())
                true
            } else {
                false
            }
        }

        editor.setOnFocusChangeListener { view, hasFocus ->
            if (isHistoryVisibile)
                onChange(Ui.History(trackHistoryAdapter.tracks))
            else
                onChange(Ui.Found(null, tracksAdapter.tracks))
        }

        findViewById<Button>(R.id.update_tracks_button).setOnClickListener {
            searchEngine.search(editor.text.toString())
        }

        clearHistoryButton.setOnClickListener {
            history.clear()
            onChange(Ui.Empty)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val json = gson.toJson(uiStateData.data)
        outState.putString(USER_SEARCH_STATE, json)
    }

    companion object {
        const val USER_SEARCH_STATE = "USER_SEARCH_STATE"
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        inputMethodManager?.hideSoftInputFromWindow(editor.windowToken, 0)
    }

    override fun onTrackClick(track: Track) {
        history.addTrack(track)
    }
}