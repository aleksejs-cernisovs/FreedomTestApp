package aleksejs.cernisovs.freedomTestApp.feature.shared.ui

import aleksejs.cernisovs.freedomTestApp.feature.ratesScreen.entity.StockQuote
import aleksejs.cernisovs.freedomTestApp.ui.theme.FreedomColor
import aleksejs.cernisovs.freedomTestApp.ui.theme.FreedomDimension
import aleksejs.cernisovs.freedomTestApp.ui.theme.FreedomFont
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
                .padding(
                    horizontal = FreedomDimension.PaddingMedium,
                    vertical = FreedomDimension.PaddingSmall
                ),
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

            Spacer(modifier = Modifier.width(FreedomDimension.PaddingSmall))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stock.ticker,
                    style = FreedomFont.TextPrimary,
                    color = FreedomColor.TextPrimary
                )

                Spacer(Modifier.size(FreedomDimension.PaddingXXXSmall))

                Text(
                    text = "${stock.exchange} | ${stock.companyName}",
                    style = FreedomFont.TextSecondary,
                    color = FreedomColor.TextGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(FreedomDimension.PaddingXSmall))

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(FreedomDimension.PaddingXXXSmall)
            ) {
                Text(
                    text = stock.lastPrice.formatPrice(),
                    style = FreedomFont.StockPrice,
                    color = if (stock.lastTradeDirection == StockQuote.PriceDirection.NONE) FreedomColor.TextSecondary else FreedomColor.White,
                    modifier = Modifier
                        .background(
                            color = when (stock.lastTradeDirection) {
                                StockQuote.PriceDirection.UP -> FreedomColor.PriceUpBg
                                StockQuote.PriceDirection.DOWN -> FreedomColor.PriceDownBg
                                StockQuote.PriceDirection.NONE -> Color.Transparent
                            },
                            shape = RoundedCornerShape(FreedomDimension.CornerRadiusDefault)
                        )
                        .padding(
                            horizontal = FreedomDimension.PaddingXXSmall,
                            vertical = FreedomDimension.PaddingXXXSmall
                        )
                )

                Text(
                    text = buildString {
                        append(stock.priceChange.formatSigned())
                        append("  ")
                        append(stock.percentChange.formatSigned())
                        append("%")
                    },
                    style = FreedomFont.TextSmall,
                    color = when {
                        stock.priceChange > 0 -> FreedomColor.Green
                        stock.priceChange < 0 -> FreedomColor.Red
                        else -> FreedomColor.Gray
                    }
                )
            }
        }

        if (shouldShowDivider) {
            HorizontalDivider(
                thickness = 1.dp,
                color = FreedomColor.Divider
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
