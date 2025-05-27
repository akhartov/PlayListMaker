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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

enum class SearchState {
    None,
    InProgress,
    NotFound,
    Found,
    Error
}

data class SearchActivityState(
    var searchRequest: String? = null,
    var foundTracks: List<Track> = listOf(),
    var searchState: SearchState = SearchState.None

)

class SearchActivity : AppCompatActivity() {
    private lateinit var editor: EditText
    private val tracksAdapter = TrackAdapter()
    private lateinit var noTracksPlaceholder: LinearLayout
    private lateinit var noTracksTextView: TextView
    private lateinit var noTracksImageView: ImageView
    private lateinit var retrySearchButton: Button


    private var currentState: SearchActivityState = SearchActivityState()
        set(value) {
            if(field == value)
                return

            field = value
            if (value.searchRequest != null)
                editor.setText(value.searchRequest)

            if (value.searchState == SearchState.Found)
                tracksAdapter.updateItems(value.foundTracks)

            updateControls()
        }

    private val retrofit = Retrofit.Builder()
        .baseUrl(ItunesApi.HOST_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(ItunesApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        editor = findViewById(R.id.inputEditText)
        noTracksPlaceholder = findViewById(R.id.placeholder_layout)
        noTracksTextView = findViewById(R.id.no_tracks_textview)
        noTracksImageView = findViewById(R.id.no_tracks_image)
        retrySearchButton = findViewById(R.id.update_tracks_button)

        findViewById<MaterialToolbar>(R.id.back).setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = tracksAdapter

        findViewById<ImageView>(R.id.clearIcon).apply {
            setOnClickListener {
                editor.setText("")
                tracksAdapter.clearItems()
                hideKeyboard()
            }
        }.also { clearIcon ->
            editor.addTextChangedListener { text ->
                clearIcon.isVisible = !text.isNullOrEmpty()
            }
        }

        editor.doAfterTextChanged { text ->
            currentState.searchRequest = text.toString()
        }

        editor.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search(tracksAdapter, v.text.toString())
                true
            } else {
                false
            }
        }

        findViewById<Button>(R.id.update_tracks_button).setOnClickListener {
            search(tracksAdapter, editor.text.toString())
        }

        savedInstanceState?.getString(USER_SEARCH_STATE)?.let {
            val dataType = object : TypeToken<SearchActivityState>() {}.type
            currentState = Gson().fromJson(it, dataType)
        }
    }

    private fun updateControls() {
        when (currentState.searchState) {
            SearchState.Error -> {
                noTracksImageView.setImageResource(R.drawable.img_no_internet_no_tracks)
                noTracksTextView.setText(resources.getString(R.string.no_internet_no_tracks))
                retrySearchButton.isVisible = true
                noTracksPlaceholder.isVisible = true
            }

            SearchState.NotFound -> {
                noTracksImageView.setImageResource(R.drawable.img_tracks_not_found)
                noTracksTextView.setText(resources.getString(R.string.tracks_not_found))
                retrySearchButton.isVisible = false
                noTracksPlaceholder.isVisible = true
            }

            else -> {
                noTracksPlaceholder.isVisible = false
            }
        }
    }

    private fun preperaRequestString(searchText: String): String {
        return searchText.trim()
    }

    private fun search(tracksAdapter: TrackAdapter, dirtySearchText: String) {
        currentState.searchState = SearchState.InProgress
        val searchText = preperaRequestString(dirtySearchText)
        if (searchText.isBlank()) {
            tracksAdapter.clearItems()
            currentState.searchState = SearchState.NotFound
            updateControls()
            return
        }

        service.search(searchText).enqueue(
            object : Callback<ItunesResponse> {
                override fun onResponse(
                    call: Call<ItunesResponse>,
                    response: Response<ItunesResponse>
                ) {
                    val foundTracks = response.body()?.tracks
                    if (foundTracks.isNullOrEmpty()) {
                        currentState.foundTracks = ArrayList()
                        tracksAdapter.clearItems()
                        currentState.searchState = SearchState.NotFound
                    } else {
                        currentState.foundTracks = foundTracks
                        tracksAdapter.updateItems(foundTracks)
                        currentState.searchState = SearchState.Found
                    }
                    updateControls()
                }

                override fun onFailure(call: Call<ItunesResponse>, t: Throwable) {
                    tracksAdapter.clearItems()
                    currentState.searchState = SearchState.Error
                    updateControls()
                }
            }
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val json = Gson().toJson(currentState)
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
}