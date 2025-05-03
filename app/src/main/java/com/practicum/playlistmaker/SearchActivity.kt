package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.appbar.MaterialToolbar

class SearchActivity : AppCompatActivity() {

    private var currentSearch = ""
    private lateinit var editor: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        editor = findViewById(R.id.inputEditText)
        currentSearch = savedInstanceState?.getString(USER_SEARCH_REQUEST).orEmpty()


        findViewById<MaterialToolbar>(R.id.back).setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val clearButton = findViewById<ImageView>(R.id.clearIcon)

        clearButton.setOnClickListener {
            editor.setText("")
            hideKeyboard()
        }

        editor.addTextChangedListener { text ->
            clearButton.isVisible = !text.isNullOrEmpty()
        }

        editor.doAfterTextChanged { text ->
            currentSearch = text.toString()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(USER_SEARCH_REQUEST, currentSearch)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        editor.setText(currentSearch)
    }

    companion object {
        const val USER_SEARCH_REQUEST = "USER_SEARCH_REQUEST"
        //const val USER_SEARCH_SELECTION_START = "USER_SEARCH_SELECTION_START"
        //const val USER_SEARCH_SELECTION_END = "USER_SEARCH_SELECTION_END"
        //const val USER_SEARCH_IS_FOCUSED = "USER_SEARCH_IS_FOCUSED"
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        inputMethodManager?.hideSoftInputFromWindow(editor.windowToken, 0)
    }
}