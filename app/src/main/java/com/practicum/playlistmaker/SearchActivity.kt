package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager

import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        findViewById<MaterialToolbar>(R.id.back).setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val inputEditText =
            findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.inputEditText)

        clearButton.setOnClickListener {
            inputEditText.setText("")
            hideKeyboard()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val noText = s.isNullOrEmpty()

                clearButton.visibility = clearButtonVisibility(noText)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        inputEditText.addTextChangedListener(textWatcher)
    }

    private fun clearButtonVisibility(noText: Boolean): Int {
        return if (noText) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        val editor =
            findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.inputEditText)
        inputMethodManager?.hideSoftInputFromWindow(editor.windowToken, 0)
    }
}