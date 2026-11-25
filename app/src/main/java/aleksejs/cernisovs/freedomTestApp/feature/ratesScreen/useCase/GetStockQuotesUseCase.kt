package aleksejs.cernisovs.freedomTestApp.feature.ratesScreen.useCase

import aleksejs.cernisovs.freedomTestApp.feature.ratesScreen.entity.StockQuote
import javax.inject.Inject

class GetStockQuotesUseCase @Inject constructor() {

    suspend operator fun invoke(): List<StockQuote> {
        return listOf(
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
    }
}
