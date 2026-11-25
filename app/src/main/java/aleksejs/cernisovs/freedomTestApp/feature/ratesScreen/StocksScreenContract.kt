package aleksejs.cernisovs.freedomTestApp.feature.ratesScreen

import aleksejs.cernisovs.freedomTestApp.feature.ratesScreen.entity.StockQuote
import androidx.compose.ui.graphics.painter.Painter

data class StocksScreenViewState(
    val quotes: List<StockQuote> = emptyList(),
    val logos: Map<String, Painter> = emptyMap(),
    val isLoading: Boolean = true,
    val isError: Boolean = false
)

sealed class StocksScreenEvent {
    object AppPausedEvent : StocksScreenEvent()
    object AppResumedEvent : StocksScreenEvent()
    object RetryButtonClickedEvent : StocksScreenEvent()
}
