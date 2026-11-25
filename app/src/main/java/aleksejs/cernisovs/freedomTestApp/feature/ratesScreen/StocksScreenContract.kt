package aleksejs.cernisovs.freedomTestApp.feature.ratesScreen

import aleksejs.cernisovs.freedomTestApp.feature.ratesScreen.entity.StockQuote

data class StocksScreenViewState(
    val quotes: List<StockQuote> = emptyList(),
    val isLoading: Boolean = true,
    val isError: Boolean = false
)

sealed class StocksScreenEvent {
    object AppPausedEvent : StocksScreenEvent()
    object AppResumedEvent : StocksScreenEvent()
    object RetryButtonClickedEvent : StocksScreenEvent()
}