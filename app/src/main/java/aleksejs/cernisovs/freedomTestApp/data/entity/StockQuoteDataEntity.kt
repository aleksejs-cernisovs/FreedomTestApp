package aleksejs.cernisovs.freedomTestApp.data.entity

data class StockQuoteDataEntity(
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
