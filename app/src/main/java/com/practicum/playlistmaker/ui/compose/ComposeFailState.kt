package com.practicum.playlistmaker.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun FailState(
    imageVector: ImageVector,
    text: String,
    onClick: () -> Unit = {},
    buttonText: String = ""
) {
    val buttonTextState by remember { mutableStateOf(buttonText) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Image(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                imageVector = imageVector,
                contentDescription = null,
                alignment = Alignment.Center
            )
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp),
                text = text,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.displayMedium
            )
            if (buttonTextState.isNotBlank()) {
                Button(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onBackground,
                        contentColor = MaterialTheme.colorScheme.surface,
                    ),
                    onClick = { onClick() }
                ) {
                    Text(text = buttonText)
                }
            }
        }
    }
}