package com.hcdisat.dailypulse.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hcdisat.dailypulse.AppScaffold
import com.hcdisat.dailypulse.Platform

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AboutScreen(onUpButtonClicked: () -> Unit = {}) {
    AppScaffold(
        toolbar = { Toolbar(onUpButtonClicked) }
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            ContentView()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Toolbar(onUpButtonClicked: () -> Unit = {}) {
    TopAppBar(
        title = { Text(text = "About Device") },
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

@Composable
private fun ContentView() {
    val items = makeItems()
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items) { (title, subtitle) ->
            RowView(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                title = title,
                subtitle = subtitle
            )
        }
    }
}

private fun makeItems(): List<Pair<String, String>> {
    val platform = Platform()
    platform.logSystemInfo()

    return listOf(
        "Operating System" to "${platform.osName} ${platform.osVersion}",
        "Device" to platform.deviceModel,
        "Density" to "${platform.density}",
    )
}

@Composable
private fun RowView(modifier: Modifier = Modifier, title: String, subtitle: String) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge,
        )
    }

    HorizontalDivider()
}
