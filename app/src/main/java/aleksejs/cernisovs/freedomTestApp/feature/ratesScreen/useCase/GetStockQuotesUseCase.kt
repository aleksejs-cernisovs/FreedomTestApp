package aleksejs.cernisovs.freedomTestApp.feature.ratesScreen.useCase

import aleksejs.cernisovs.freedomTestApp.data.entity.StockQuoteDataEntity
import aleksejs.cernisovs.freedomTestApp.data.repository.StocksRepository
import aleksejs.cernisovs.freedomTestApp.feature.ratesScreen.entity.StockQuote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val tickers = listOf(
    "SP500.IDX", "AAPL.US", "RSTI", "GAZP", "MRKZ", "RUAL", "HYDR", "MRKS",
    "SBER", "FEES", "TGKA", "VTBR", "ANH.US", "VICL.US", "BURG.US", "NBL.US",
    "YETI.US", "WSFS.US", "NIO.US", "DXC", "MIC.US", "HSBC.US", "EXPN.EU",
    "GSK.EU", "SHP.EU", "MAN.EU", "DB1.EU", "MUV2.EU", "TATE.EU", "KGF.EU",
    "MGGT.EU", "SGGD.EU", "AI.US"
)

class GetStockQuotesUseCase @Inject constructor(
    private val stocksRepository: StocksRepository
) {
    operator fun invoke(): Flow<List<StockQuote>> {
        return stocksRepository.getStockQuotes(tickers).map { entities ->
            entities.map { entity -> entity.toUiEntity() }
        }
    }

    private fun StockQuoteDataEntity.toUiEntity(): StockQuote {
        return StockQuote(
            ticker = ticker,
            exchange = exchange,
            companyName = companyName,
            currency = currency,
            lastPrice = lastPrice,
            priceChange = priceChange,
            percentChange = percentChange,
            lastTradeDirection = when (lastTradeDirection) {
                StockQuoteDataEntity.PriceDirection.UP -> StockQuote.PriceDirection.UP
                StockQuoteDataEntity.PriceDirection.DOWN -> StockQuote.PriceDirection.DOWN
                StockQuoteDataEntity.PriceDirection.NONE -> StockQuote.PriceDirection.NONE
            }
        )
    }
}
