package com.hcdisat.dailypulse.sources.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(screenName: String, onUpButtonClicked: () -> Unit = {}) {
    TopAppBar(
        title = { Text(text = screenName) },
        navigationIcon = {
            IconButton(onUpButtonClicked) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Up Button"
                )
            }
        }
    )
}