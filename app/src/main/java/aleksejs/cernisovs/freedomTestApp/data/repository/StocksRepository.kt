package aleksejs.cernisovs.freedomTestApp.data.repository

import aleksejs.cernisovs.freedomTestApp.data.entity.StockQuoteDataEntity
import kotlinx.coroutines.flow.Flow

interface StocksRepository {
    fun getStockQuotes(tickers: List<String>): Flow<List<StockQuoteDataEntity>>
    suspend fun getStockLogo(ticker: String): ByteArray?
}
