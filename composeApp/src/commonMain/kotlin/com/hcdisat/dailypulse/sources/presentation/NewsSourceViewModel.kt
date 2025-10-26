package com.hcdisat.dailypulse.sources.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hcdisat.dailypulse.core.dataaccess.network.logger
import com.hcdisat.dailypulse.core.model.NewsCategory
import com.hcdisat.dailypulse.sources.domain.model.Source
import com.hcdisat.dailypulse.sources.domain.model.SourceConfig
import com.hcdisat.dailypulse.sources.domain.usecase.GetSourceConfigUseCase
import com.hcdisat.dailypulse.sources.domain.usecase.LoadNewsSourcesUseCase
import com.hcdisat.dailypulse.sources.domain.usecase.UpdateCategoryUseCase
import com.hcdisat.dailypulse.sources.domain.usecase.UpdateNewsSourceUseCase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewsSourceViewModel(
    getSourcesConfig: GetSourceConfigUseCase,
    private val updateCategory: UpdateCategoryUseCase,
    private val loadSources: LoadNewsSourcesUseCase,
    private val updateSource: UpdateNewsSourceUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _uiState = MutableStateFlow(NewsSourceState())
    val uiState = _uiState.asStateFlow()

    init {
        getSourcesConfig()
            .onEach { sourceConfig -> updateConfig(sourceConfig) }
            .catch { logger().e(it) { it.message.toString() } }
            .flowOn(dispatcher)
            .launchIn(viewModelScope)
    }

    fun onEvent(event: SourceEvent) {
        when (event) {
            is SourceEvent.UpdateCategory -> updateNewsCategory(event.category)
            is SourceEvent.LoadRemoteResources -> loadRemoteSources()
            is SourceEvent.UpdateNewsSource -> updateNewsSource(event.source)
        }
    }

    private fun updateNewsSource(source: SelectedSource) =
        viewModelScope.launch(dispatcher) {
            updateSource(source.id, source.heading)
        }

    private fun loadRemoteSources() = viewModelScope.launch(dispatcher) {
        _uiState.update { it.copy(sources = persistentListOf()) }
        loadSources(uiState.value.selectedCategory).fold(
            onSuccess = { sources ->
                _uiState.update { state ->
                    state.copy(
                        errorMessage = null,
                        sources = sources.map { it.toUISource() }.toPersistentList()
                    )
                }
            },
            onFailure = {
                ensureActive()
                logger().e(it) { it.message.toString() }
                _uiState.update { state ->
                    state.copy(errorMessage = "Failed loading news sources")
                }
            }
        )
    }

    private fun updateNewsCategory(category: NewsCategory) =
        viewModelScope.launch(dispatcher) { updateCategory(category) }

    private fun updateConfig(sourceConfig: SourceConfig) {
        _uiState.update {
            it.copy(
                selectedCategory = NewsCategory.getNewsCategory(sourceConfig.category),
                selectedSource = SelectedSource(
                    id = sourceConfig.sourceId.orEmpty(),
                    heading = sourceConfig.sourceName.orEmpty().ifEmpty { UISource.EMPTY_SOURCE }
                )
            )
        }
    }
}

data class NewsSourceState(
    val selectedCategory: NewsCategory = NewsCategory.GENERAL,
    val selectedSource: SelectedSource = SelectedSource(),
    val categories: ImmutableList<NewsCategory> = NewsCategory.entries.toPersistentList(),
    val sources: ImmutableList<UISource> = persistentListOf(),
    val errorMessage: String? = null
)

data class SelectedSource(
    val id: String = "",
    val heading: String = "None"
)

data class UISource(
    val id: String = "",
    val heading: String = EMPTY_SOURCE,
    val description: String = "",
    val language: String = ""
) {
    companion object {
        const val EMPTY_SOURCE = "NONE"
    }
}

sealed interface SourceEvent {
    data object LoadRemoteResources : SourceEvent
    data class UpdateCategory(val category: NewsCategory) : SourceEvent
    data class UpdateNewsSource(val source: SelectedSource) : SourceEvent
}

private fun Source.toUISource(): UISource = UISource(
    id = id,
    heading = name,
    description = description,
    language = language
)