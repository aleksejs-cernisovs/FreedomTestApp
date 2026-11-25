package aleksejs.cernisovs.freedomtestapp

import aleksejs.cernisovs.freedomtestapp.feature.ratesScreen.RatesScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import aleksejs.cernisovs.freedomtestapp.ui.theme.FreedomTestAppTheme

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