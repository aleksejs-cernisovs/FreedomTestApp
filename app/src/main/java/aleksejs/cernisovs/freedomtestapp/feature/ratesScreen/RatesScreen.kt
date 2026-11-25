package aleksejs.cernisovs.freedomtestapp.feature.ratesScreen

import aleksejs.cernisovs.freedomtestapp.R
import aleksejs.cernisovs.freedomtestapp.feature.shared.ui.StockListItem
import aleksejs.cernisovs.freedomtestapp.ui.theme.FreedomColor
import aleksejs.cernisovs.freedomtestapp.ui.theme.FreedomDimension
import aleksejs.cernisovs.freedomtestapp.ui.theme.FreedomFont
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RatesScreen(
    modifier: Modifier = Modifier
) {
    QuotesScreenContent(
        screenData = QuotesScreenViewData(dummyQuoteList),
        modifier = modifier
    )
}

@Composable
private fun QuotesScreenContent(
    screenData: QuotesScreenViewData,
    modifier: Modifier = Modifier
) {
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

@Composable
@Preview
private fun QuotesScreenPreview() {
    QuotesScreenContent(
        QuotesScreenViewData(
            dummyQuoteList
        )
    )
}

data class QuotesScreenViewData(
    val quotes: List<StockQuote>
)

data class StockQuote(
    val ticker: String,
    val exchange: String,
    val companyName: String,
    val currency: String,
    val lastPrice: Double,
    val priceChange: Double,
    val percentChange: Double,
    val lastTradeDirection: PriceDirection
) {
    enum class PriceDirection {
        UP,
        DOWN,
        NONE
    }
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
        companyName = "NVidia Inc.",
        currency = "USD",
        lastPrice = 12.90,
        priceChange = -300.3,
        percentChange = -96.5,
        lastTradeDirection = StockQuote.PriceDirection.DOWN
    )
)
