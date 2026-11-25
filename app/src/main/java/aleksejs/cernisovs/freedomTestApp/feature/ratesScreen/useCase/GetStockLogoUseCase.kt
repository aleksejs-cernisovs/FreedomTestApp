package aleksejs.cernisovs.freedomTestApp.feature.ratesScreen.useCase

import aleksejs.cernisovs.freedomTestApp.data.repository.StocksRepository
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import javax.inject.Inject

class GetStockLogoUseCase @Inject constructor(
    private val stocksRepository: StocksRepository
) {
    suspend operator fun invoke(ticker: String): Painter? {
        val bytes = stocksRepository.getStockLogo(ticker) ?: return null
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size) ?: return null
        return BitmapPainter(bitmap.asImageBitmap())
    }
}
