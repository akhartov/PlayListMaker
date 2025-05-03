package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class SearchActivity : AppCompatActivity() {

    private var currentSearch = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        if (savedInstanceState != null) {
            currentSearch = savedInstanceState.getString(USER_SEARCH_REQUEST).toString()
        } else {
            currentSearch = ""
        }

        findViewById<MaterialToolbar>(R.id.back).setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val editor = textEditor()

        clearButton.setOnClickListener {
            editor.setText("")
            hideKeyboard()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val noText = s.isNullOrEmpty()

                clearButton.visibility = if (noText) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                currentSearch = s.toString()
            }
        }

        editor.addTextChangedListener(textWatcher)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(USER_SEARCH_REQUEST, currentSearch)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        //super.onRestoreInstanceState(savedInstanceState)

        textEditor().setText(currentSearch)
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

        inputMethodManager?.hideSoftInputFromWindow(textEditor().windowToken, 0)
    }

    private fun textEditor(): EditText {
        return findViewById<EditText>(R.id.inputEditText)
    }
}