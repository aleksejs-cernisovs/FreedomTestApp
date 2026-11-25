package aleksejs.cernisovs.freedomtestapp.feature.ratesScreen

import aleksejs.cernisovs.freedomtestapp.R
import aleksejs.cernisovs.freedomtestapp.feature.ratesScreen.entity.StockQuote
import aleksejs.cernisovs.freedomtestapp.feature.shared.ui.StockListItem
import aleksejs.cernisovs.freedomtestapp.ui.theme.FreedomColor
import aleksejs.cernisovs.freedomtestapp.ui.theme.FreedomDimension
import aleksejs.cernisovs.freedomtestapp.ui.theme.FreedomFont
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RatesScreen(
    modifier: Modifier = Modifier,
    viewModel: StocksScreenViewModel = viewModel()
) {
    val screenData by viewModel.viewState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner, viewModel) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> viewModel.onEvent(StocksScreenEvent.AppPausedEvent)
                Lifecycle.Event.ON_RESUME -> viewModel.onEvent(StocksScreenEvent.AppResumedEvent)
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    StocksScreenContent(
        screenData = screenData,
        modifier = modifier
    )
}

@Composable
private fun StocksScreenContent(
    screenData: StocksScreenViewState,
    modifier: Modifier = Modifier
) {

    if (screenData.isLoading) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(FreedomColor.White),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(FreedomColor.White)
                .padding(bottom = FreedomDimension.PaddingMedium)
        ) {

            item {
                Spacer(Modifier.size(FreedomDimension.PaddingXLarge))
            }

            item {
                Text(
                    text = stringResource(R.string.recommended_rates),
                    style = FreedomFont.TextPrimary,
                    color = FreedomColor.TextPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Spacer(Modifier.size(FreedomDimension.PaddingXLarge))
            }

            itemsIndexed(items = screenData.quotes) { index, quoteData ->
                StockListItem(
                    stock = quoteData,
                    shouldShowDivider = index != (screenData.quotes.size - 1)
                )
            }
        }
    }
}

@Composable
@Preview
private fun StocksScreenPreview() {
    StocksScreenContent(
        StocksScreenViewState(
            dummyQuoteList,
            isLoading = false
        )
    )
}

private val dummyQuoteList = listOf(
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
        companyName = "NVIDIA Inc.",
        currency = "USD",
        lastPrice = 12.90,
        priceChange = -300.3,
        percentChange = -96.5,
        lastTradeDirection = StockQuote.PriceDirection.DOWN
    )
)
