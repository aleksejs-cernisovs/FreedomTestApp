package aleksejs.cernisovs.freedomtestapp.feature.ratesScreen

import aleksejs.cernisovs.freedomtestapp.feature.ratesScreen.entity.StockQuote
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StocksScreenViewModel : ViewModel() {

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
        updateState {
            it.copy(
                isLoading = false, quotes = listOf(
                    StockQuote(
                        ticker = "AAPL.US",
                        exchange = "FIX",
                        companyName = "Apple Inc.",
                        currency = "USD",
                        lastPrice = 381.35,
                        priceChange = -3.58,
                        percentChange = -1.02,
                        lastTradeDirection = StockQuote.PriceDirection.NONE
                    ),
                    StockQuote(
                        ticker = "TSLA.US",
                        exchange = "FIX",
                        companyName = "Tesla Inc.",
                        currency = "USD",
                        lastPrice = 347.90,
                        priceChange = +13.58,
                        percentChange = +8.16,
                        lastTradeDirection = StockQuote.PriceDirection.UP
                    ),
                    StockQuote(
                        ticker = "NVIDIA.US",
                        exchange = "FIX",
                        companyName = "NVidia Inc.",
                        currency = "USD",
                        lastPrice = 12.90,
                        priceChange = -300.3,
                        percentChange = -96.5,
                        lastTradeDirection = StockQuote.PriceDirection.DOWN
                    )
                )
            )
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
