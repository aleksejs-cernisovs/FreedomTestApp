package aleksejs.cernisovs.freedomTestApp

import aleksejs.cernisovs.freedomTestApp.feature.ratesScreen.RatesScreen
import aleksejs.cernisovs.freedomTestApp.ui.theme.FreedomTestAppTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FreedomTestAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RatesScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
