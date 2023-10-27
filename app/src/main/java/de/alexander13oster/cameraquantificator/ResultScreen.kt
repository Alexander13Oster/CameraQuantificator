package de.alexander13oster.cameraquantificator

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ResultScreen(
    result: String,
) {
    Text(text = result)
}