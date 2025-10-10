package com.hcdisat.dailypulse.about

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.hcdisat.dailypulse.AppScaffold
import com.hcdisat.dailypulse.core.presentation.WindowInfo
import com.hcdisat.dailypulse.core.presentation.rememberWindowInfo
import com.hcdisat.dailypulse.getPlatform
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Preview
@Composable
fun AdaptiveAboutScreen(onUpButtonClicked: () -> Unit = {}) {
    AdaptiveAboutScreen(
        windowInfo = rememberWindowInfo(),
        onUpButtonClicked = onUpButtonClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdaptiveAboutScreen(
    windowInfo: WindowInfo,
    onUpButtonClicked: () -> Unit = {}
) {
    AppScaffold(
        toolbar = { Toolbar(onUpButtonClicked) }
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            when (windowInfo.widthInfo) {
                WindowInfo.WindowType.Compact -> ContentViewCompact()
                WindowInfo.WindowType.Medium -> ContentViewMedium()
                WindowInfo.WindowType.Expanded -> ContentViewExpanded()
            }
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
@Preview(showBackground = true)
private fun ContentViewCompact() {
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

@Composable
@Preview(showBackground = true)
private fun ContentViewMedium() {
    val items = makeItems()
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items) { (title, subtitle) ->
            RowView(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                title = title,
                subtitle = subtitle,
                titleStyle = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun ContentViewExpanded() {
    val items = makeItems()
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(32.dp)
            .shadow(
                elevation = 1.dp,
                shape = MaterialTheme.shapes.extraSmall,
                spotColor = MaterialTheme.colorScheme.onSurface
            )
    ) {
        LazyColumn {
            items(items) { (title, subtitle) ->
                RowView(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    title = title,
                    subtitle = subtitle,
                    titleStyle = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

private fun makeItems(): List<Pair<String, String>> {
    val platform = getPlatform()
    platform.logSystemInfo()

    return listOf(
        "Operating System" to "${platform.osName} ${platform.osVersion}",
        "Device" to platform.deviceModel,
        "Density" to "${platform.density}",
    )
}

@Composable
private fun RowView(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    titleStyle: TextStyle = MaterialTheme.typography.labelSmall,
    titleColor: Color = MaterialTheme.colorScheme.primary,
    subtitleStyle: TextStyle = MaterialTheme.typography.bodyLarge,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = titleStyle,
            color = titleColor
        )

        Text(
            text = subtitle,
            style = subtitleStyle,
        )
    }

    HorizontalDivider()
}
