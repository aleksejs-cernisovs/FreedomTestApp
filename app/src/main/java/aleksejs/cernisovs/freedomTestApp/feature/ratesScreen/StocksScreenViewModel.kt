package aleksejs.cernisovs.freedomTestApp.feature.ratesScreen

import aleksejs.cernisovs.freedomTestApp.feature.ratesScreen.useCase.GetStockQuotesUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StocksScreenViewModel @Inject constructor(
    private val getStockQuotesUseCase: GetStockQuotesUseCase
) : ViewModel() {

    private val viewStateFlow = MutableStateFlow(StocksScreenViewState())
    val viewState: StateFlow<StocksScreenViewState> = viewStateFlow

    private var pollingJob: Job? = null

    init {
        updateState {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            delay(3_000)
            refreshQuotes()
            startPolling()
        }
    }

    fun onEvent(event: StocksScreenEvent) {
        when (event) {
            StocksScreenEvent.AppPausedEvent -> {
                stopPolling()
            }

            StocksScreenEvent.AppResumedEvent -> {
                if (pollingJob == null) {
                    startPolling()
                }
            }

            StocksScreenEvent.PullToRefreshTriggeredEvent -> {
                refreshQuotes()
            }
        }
    }

    private fun refreshQuotes() {
        viewModelScope.launch {
            val quotes = getStockQuotesUseCase()
            updateState {
                it.copy(
                    isLoading = false,
                    quotes = quotes
                )
            }
        }
    }

    private fun startPolling() {
        if (pollingJob?.isActive == true) return
        pollingJob = viewModelScope.launch {
            while (true) {
                delay(3_000)
                refreshQuotes()
            }
        }
    }

    private fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
    }

    private inline fun StocksScreenViewModel.updateState(
        update: (StocksScreenViewState) -> StocksScreenViewState
    ) {
        viewStateFlow.value = update(viewStateFlow.value)
    }
}
