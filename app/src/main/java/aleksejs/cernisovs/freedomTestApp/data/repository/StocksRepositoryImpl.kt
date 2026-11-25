package aleksejs.cernisovs.freedomTestApp.data.repository

import aleksejs.cernisovs.freedomTestApp.data.entity.StockQuoteDataEntity
import aleksejs.cernisovs.freedomTestApp.data.exception.ConnectionFailedException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

private const val TRADENET_SERVER_URL = "wss://wss.tradernet.com"

@Singleton
class StocksRepositoryImpl @Inject constructor(
    private val client: OkHttpClient
) : StocksRepository {

    override fun getStockQuotes(tickers: List<String>): Flow<List<StockQuoteDataEntity>> =
        callbackFlow {
            val quotesMap = LinkedHashMap<String, StockQuoteDataEntity>()

            tickers.forEach { ticker ->
                quotesMap[ticker] = StockQuoteDataEntity(
                    ticker = ticker,
                    exchange = "",
                    companyName = ticker,
                    currency = "USD",
                    lastPrice = 0.0,
                    priceChange = 0.0,
                    percentChange = 0.0,
                    lastTradeDirection = StockQuoteDataEntity.PriceDirection.NONE
                )
            }

            val request = Request.Builder()
                .url(TRADENET_SERVER_URL)
                .build()

            val listener = object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    val subscriptionArray = JSONArray().apply {
                        put("quotes")
                        put(JSONArray(tickers))
                    }
                    webSocket.send(subscriptionArray.toString())
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    try {
                        val jsonArray = JSONArray(text)
                        if (jsonArray.length() >= 2 && jsonArray.getString(0) == "q") {
                            val quoteJson = jsonArray.getJSONObject(1)
                            val ticker = quoteJson.getStringOrNull("c")
                            if (ticker != null && quotesMap.containsKey(ticker)) {
                                val existing = quotesMap[ticker]!!
                                val newPrice =
                                    quoteJson.getDoubleOrNull("ltp") ?: existing.lastPrice

                                val priceDirection = when {
                                    existing.lastPrice == 0.0 -> StockQuoteDataEntity.PriceDirection.NONE
                                    newPrice > existing.lastPrice -> StockQuoteDataEntity.PriceDirection.UP
                                    newPrice < existing.lastPrice -> StockQuoteDataEntity.PriceDirection.DOWN
                                    else -> existing.lastTradeDirection
                                }

                                val updatedQuote = existing.copy(
                                    exchange = quoteJson.getStringOrNull("ltr")
                                        ?: existing.exchange,
                                    companyName = quoteJson.getStringOrNull("name")
                                        ?: quoteJson.getStringOrNull("name2")
                                        ?: existing.companyName,
                                    currency = quoteJson.getStringOrNull("curr")
                                        ?: existing.currency,
                                    lastPrice = newPrice,
                                    priceChange = quoteJson.getDoubleOrNull("chg")
                                        ?: existing.priceChange,
                                    percentChange = quoteJson.getDoubleOrNull("pcp")
                                        ?: existing.percentChange,
                                    lastTradeDirection = priceDirection
                                )

                                quotesMap[ticker] = updatedQuote
                                trySend(quotesMap.values.toList())
                            }
                        }
                    } catch (_: Exception) {
                    }
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    close(ConnectionFailedException())
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {}
            }

            val webSocket = client.newWebSocket(request, listener)

            awaitClose {
                webSocket.close(1000, "Flow cancelled")
            }
        }

    private fun JSONObject.getDoubleOrNull(key: String): Double? {
        if (!has(key) || isNull(key)) return null
        val doubleVal = optDouble(key)
        if (!doubleVal.isNaN()) return doubleVal
        return optString(key).toDoubleOrNull()
    }

    private fun JSONObject.getStringOrNull(key: String): String? {
        if (!has(key) || isNull(key)) return null
        return optString(key).takeIf { it.isNotEmpty() && it != "null" }
    }
}
