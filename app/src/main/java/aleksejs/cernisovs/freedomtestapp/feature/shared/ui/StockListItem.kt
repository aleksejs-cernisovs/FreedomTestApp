package aleksejs.cernisovs.freedomtestapp.feature.shared.ui

import aleksejs.cernisovs.freedomtestapp.feature.ratesScreen.StockQuote
import aleksejs.cernisovs.freedomtestapp.ui.theme.FreedomDivider
import aleksejs.cernisovs.freedomtestapp.ui.theme.FreedomGray
import aleksejs.cernisovs.freedomtestapp.ui.theme.FreedomGreen
import aleksejs.cernisovs.freedomtestapp.ui.theme.FreedomPriceDownBg
import aleksejs.cernisovs.freedomtestapp.ui.theme.FreedomPriceUpBg
import aleksejs.cernisovs.freedomtestapp.ui.theme.FreedomRed
import aleksejs.cernisovs.freedomtestapp.ui.theme.FreedomTextGray
import aleksejs.cernisovs.freedomtestapp.ui.theme.FreedomTextPrimary
import aleksejs.cernisovs.freedomtestapp.ui.theme.FreedomTextSecondary
import aleksejs.cernisovs.freedomtestapp.ui.theme.FreedomWhite
import aleksejs.cernisovs.freedomtestapp.ui.theme.TextPrimary
import aleksejs.cernisovs.freedomtestapp.ui.theme.TextSecondary
import aleksejs.cernisovs.freedomtestapp.ui.theme.TextSmall
import aleksejs.cernisovs.freedomtestapp.ui.theme.cornerRadiusDefault
import aleksejs.cernisovs.freedomtestapp.ui.theme.paddingMedium
import aleksejs.cernisovs.freedomtestapp.ui.theme.paddingSmall
import aleksejs.cernisovs.freedomtestapp.ui.theme.paddingXSmall
import aleksejs.cernisovs.freedomtestapp.ui.theme.paddingXXSmall
import aleksejs.cernisovs.freedomtestapp.ui.theme.paddingXXXSmall
import aleksejs.cernisovs.freedomtestapp.ui.theme.stockPrice
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun StockListItem(
    stock: StockQuote,
    modifier: Modifier = Modifier,
    logo: Painter? = null,
    shouldShowDivider: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = paddingMedium, vertical = paddingSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {

            val iconSize = 26.dp
            if (logo != null) {
                Image(
                    painter = logo,
                    contentDescription = null,
                    modifier = Modifier.size(iconSize),
                    contentScale = ContentScale.Fit
                )
            } else {
                Spacer(modifier = Modifier.width(iconSize))
            }

            Spacer(modifier = Modifier.width(paddingSmall))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stock.ticker,
                    style = TextPrimary,
                    color = FreedomTextPrimary
                )

                Spacer(Modifier.size(paddingXXXSmall))

                Text(
                    text = "${stock.exchange} | ${stock.companyName}",
                    style = TextSecondary,
                    color = FreedomTextGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(paddingXSmall))

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(paddingXXXSmall)
            ) {
                Text(
                    text = stock.lastPrice.formatPrice(),
                    style = stockPrice,
                    color = if (stock.lastTradeDirection == StockQuote.PriceDirection.NONE) FreedomTextSecondary else FreedomWhite,
                    modifier = Modifier
                        .background(
                            color = when (stock.lastTradeDirection) {
                                StockQuote.PriceDirection.UP -> FreedomPriceUpBg
                                StockQuote.PriceDirection.DOWN -> FreedomPriceDownBg
                                StockQuote.PriceDirection.NONE -> Color.Transparent
                            },
                            shape = RoundedCornerShape(cornerRadiusDefault)
                        )
                        .padding(horizontal = paddingXXSmall, vertical = paddingXXXSmall)
                )

                Text(
                    text = buildString {
                        append(stock.priceChange.formatSigned())
                        append("  ")
                        append(stock.percentChange.formatSigned())
                        append("%")
                    },
                    style = TextSmall,
                    color = when {
                        stock.priceChange > 0 -> FreedomGreen
                        stock.priceChange < 0 -> FreedomRed
                        else -> FreedomGray
                    }
                )
            }
        }

        if (shouldShowDivider) {
            HorizontalDivider(
                thickness = 1.dp,
                color = FreedomDivider
            )
        }
    }
}

private fun Double.formatPrice(): String {
    return String.format(
        Locale.US,
        "%,.2f",
        this
    )
}

private fun Double.formatSigned(): String {
    return when {
        this > 0 -> "+${String.format(Locale.US, "%.2f", this)}"
        else -> String.format(Locale.US, "%.2f", this)
    }
}

@Composable
@Preview
private fun StockListItemPreview() {
    StockListItem(
        StockQuote(
            ticker = "AAPL.US",
            exchange = "FIX",
            companyName = "Apple Inc.",
            currency = "USD",
            lastPrice = 381.35,
            priceChange = -3.58,
            percentChange = -1.02,
            lastTradeDirection = StockQuote.PriceDirection.DOWN
        ),
        shouldShowDivider = true
    )
}
