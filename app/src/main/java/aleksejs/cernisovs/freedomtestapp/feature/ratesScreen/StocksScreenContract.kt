package aleksejs.cernisovs.freedomtestapp.feature.ratesScreen

import aleksejs.cernisovs.freedomtestapp.feature.ratesScreen.entity.StockQuote

data class StocksScreenViewState(
    val quotes: List<StockQuote> = emptyList(),
    val isLoading: Boolean = true
)

sealed class StocksScreenEvent {
    object PullToRefreshTriggeredEvent : StocksScreenEvent()
    object AppPausedEvent : StocksScreenEvent()
    object AppResumedEvent : StocksScreenEvent()
}