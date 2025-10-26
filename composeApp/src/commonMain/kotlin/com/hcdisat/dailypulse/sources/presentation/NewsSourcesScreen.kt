@file:OptIn(ExperimentalMaterial3Api::class)

package com.hcdisat.dailypulse.sources.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hcdisat.dailypulse.AppScaffold
import com.hcdisat.dailypulse.Route
import com.hcdisat.dailypulse.core.model.NewsCategory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun NewsSourceConfigScreen(
    modifier: Modifier = Modifier,
    state: NewsSourceState = NewsSourceState(),
    onRouteEvent: (Route.Sources.RouteAction) -> Unit = {},
) {
    val scope = rememberCoroutineScope()

    val categorySheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showCategorySheet by remember { mutableStateOf(false) }

    val sourcesSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSourcesSheet by remember { mutableStateOf(false) }

    AppScaffold(
        toolbar = {
            Toolbar("Config News Sources") {
                onRouteEvent(Route.Sources.RouteAction.NavigateUp)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).then(modifier),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (showCategorySheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showCategorySheet = false
                    },
                    sheetState = categorySheetState
                ) {
                    Category(
                        modifier = Modifier.padding(bottom = 32.dp),
                        selectedCategory = state.selectedCategory,
                        categories = state.categories
                    ) { category ->
                        onRouteEvent(
                            Route.Sources.RouteAction.CategorySelected(
                                category = category
                            )
                        )

                        scope.launch { categorySheetState.hide() }.invokeOnCompletion {
                            if (!categorySheetState.isVisible) {
                                showCategorySheet = false
                            }
                        }
                    }
                }
            }

            SourceConfigButton(
                modifier = Modifier.padding(16.dp),
                label = "Category",
                value = state.selectedCategory.value,
                onClick = {
                    showSourcesSheet = false
                    showCategorySheet = true
                }
            )

            SourceConfigButton(
                modifier = Modifier.padding(16.dp),
                label = "Selected Source",
                value = state.selectedSource.heading,
                onClick = {
                    showCategorySheet = false
                    showSourcesSheet = true
                    onRouteEvent(Route.Sources.RouteAction.LoadRemoteSources)
                }
            )

            if (showSourcesSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showSourcesSheet = false
                    },
                    shape = RectangleShape,
                    sheetState = sourcesSheetState,
                ) {
                    AvailableSources(
                        modifier = Modifier.padding(vertical = 16.dp),
                        sources = state.sources,
                        selectedSourceId = state.selectedSource.id,
                        errorMessage = state.errorMessage
                    ) {
                        onRouteEvent(
                            Route.Sources.RouteAction.SourceSelected(
                                source = SelectedSource(
                                    id = it.id,
                                    heading = it.heading
                                )
                            )
                        )

                        scope.launch { sourcesSheetState.hide() }.invokeOnCompletion {
                            if (!sourcesSheetState.isVisible) {
                                showSourcesSheet = false
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun AvailableSources(
    modifier: Modifier = Modifier,
    sources: ImmutableList<UISource> = persistentListOf(),
    selectedSourceId: String = "",
    errorMessage: String? = null,
    onSourceSelected: (UISource) -> Unit = {}
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        when {
            errorMessage != null -> {
                item {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(32.dp).fillParentMaxSize(),
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            text = errorMessage,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            sources.isEmpty() -> {
                item {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(32.dp).fillParentMaxSize(),
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            trackColor = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.width(64.dp)
                        )
                    }
                }
            }

            else -> {
                items(sources, key = { it.id }) { source ->
                    SourceCard(
                        selectedSourceId = selectedSourceId,
                        source = source,
                        onSourceSelected = onSourceSelected
                    )
                }
            }
        }
    }
}

@Composable
private fun SourceConfigButton(
    modifier: Modifier = Modifier,
    label: String = "Category",
    value: String = "business",
    labelColor: Color = MaterialTheme.colorScheme.onPrimary,
    labelTextStyle: TextStyle = MaterialTheme.typography
        .bodyMedium.copy(fontWeight = FontWeight.Medium),
    valueColor: Color = MaterialTheme.colorScheme.primary,
    valueTextStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .border(width = 1.dp, color = MaterialTheme.colorScheme.primary)
            .clickable(interactionSource = interactionSource) { onClick() }
    ) {
        Text(
            text = label,
            style = labelTextStyle,
            color = labelColor,
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(8.dp)
        )

        Text(
            text = value,
            style = valueTextStyle,
            color = valueColor,
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(8.dp)
        )
    }
}

@Composable
private fun SourceCard(
    modifier: Modifier = Modifier,
    selectedSourceId: String,
    source: UISource,
    onSourceSelected: (UISource) -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                modifier = Modifier.padding(0.dp),
                checked = source.id == selectedSourceId,
                onCheckedChange = {
                    onSourceSelected(source)
                }
            )

            Text(
                text = source.heading,
                style = MaterialTheme.typography.labelLarge
            )
        }

        Text(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            text = source.description,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            text = source.language,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.End
        )
    }
}


@Composable
private fun Category(
    modifier: Modifier = Modifier,
    selectedCategory: NewsCategory,
    categories: ImmutableList<NewsCategory> = persistentListOf(),
    onCategorySelected: (NewsCategory) -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .selectableGroup()
            .then(modifier)
    ) {
        categories.forEach { categoryOption ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                RadioButton(
                    selected = categoryOption == selectedCategory,
                    onClick = { onCategorySelected(categoryOption) }
                )
                Text(categoryOption.value)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun NewsSourceConfigScreenPreview() {
    MaterialTheme {
        NewsSourceConfigScreen()
    }
}
