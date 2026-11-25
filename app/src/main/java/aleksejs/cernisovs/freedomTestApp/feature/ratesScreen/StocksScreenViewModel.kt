package aleksejs.cernisovs.freedomTestApp.feature.ratesScreen

import aleksejs.cernisovs.freedomTestApp.feature.ratesScreen.useCase.GetStockLogoUseCase
import aleksejs.cernisovs.freedomTestApp.feature.ratesScreen.useCase.GetStockQuotesUseCase
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StocksScreenViewModel @Inject constructor(
    private val getStockQuotesUseCase: GetStockQuotesUseCase,
    private val getStockLogoUseCase: GetStockLogoUseCase
) : ViewModel() {

    private val viewStateFlow = MutableStateFlow(StocksScreenViewState())
    val viewState: StateFlow<StocksScreenViewState> = viewStateFlow

    private var quotesJob: Job? = null
    private var hasFetchedLogos = false

    init {
        updateState {
            it.copy(isLoading = true)
        }
        startCollecting()
    }

    fun onEvent(event: StocksScreenEvent) {
        when (event) {
            StocksScreenEvent.AppPausedEvent -> {
                stopCollecting()
            }

            StocksScreenEvent.AppResumedEvent -> {
                if (quotesJob == null && !viewState.value.isError) {
                    startCollecting()
                }
            }

            StocksScreenEvent.RetryButtonClickedEvent -> {
                stopCollecting()
                startCollecting()
            }
        }
    }

    private fun startCollecting() {
        if (quotesJob?.isActive == true) return

        updateState {
            it.copy(
                isError = false,
                isLoading = it.quotes.isEmpty()
            )
        }
        quotesJob = viewModelScope.launch {
            getStockQuotesUseCase()
                .catch {
                    updateState {
                        it.copy(
                            isLoading = false,
                            isError = true
                        )
                    }
                }
                .collect { quotes ->
                    updateState {
                        it.copy(
                            isLoading = false,
                            isError = false,
                            quotes = quotes
                        )
                    }

                    fetchLogosIfNeed()
                }
        }
    }

    private fun fetchLogosIfNeed() {
        if (hasFetchedLogos) return
        hasFetchedLogos = true

        val currentQuotes = viewState.value.quotes
        if (currentQuotes.isEmpty()) return

        viewModelScope.launch(Dispatchers.IO) {
            val loadedLogos = mutableMapOf<String, Painter>()
            currentQuotes.forEach { quote ->
                val logoPainter = getStockLogoUseCase(quote.ticker)
                if (logoPainter != null) {
                    loadedLogos[quote.ticker] = logoPainter
                }
            }
            if (loadedLogos.isNotEmpty()) {
                updateState { state ->
                    state.copy(logos = state.logos + loadedLogos)
                }
            }
        }
    }

    private fun stopCollecting() {
        quotesJob?.cancel()
        quotesJob = null
    }

    private inline fun StocksScreenViewModel.updateState(
        crossinline update: (StocksScreenViewState) -> StocksScreenViewState
    ) {
        viewStateFlow.update { update(it) }
    }
}
