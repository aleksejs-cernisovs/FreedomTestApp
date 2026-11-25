package aleksejs.cernisovs.freedomTestApp.feature.ratesScreen

import aleksejs.cernisovs.freedomTestApp.feature.ratesScreen.useCase.GetStockQuotesUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StocksScreenViewModel @Inject constructor(
    private val getStockQuotesUseCase: GetStockQuotesUseCase
) : ViewModel() {

    private val viewStateFlow = MutableStateFlow(StocksScreenViewState())
    val viewState: StateFlow<StocksScreenViewState> = viewStateFlow

    private var quotesJob: Job? = null

    init {
        updateState {
            it.copy(isLoading = true)
        }
        startCollecting()
    }

    fun onEvent(event: StocksScreenEvent) {
        when (event) {
            StocksScreenEvent.AppPausedEvent -> {
                stopCollecting()
            }

            StocksScreenEvent.AppResumedEvent -> {
                if (quotesJob == null) {
                    startCollecting()
                }
            }
        }
    }

    private fun startCollecting() {
        if (quotesJob?.isActive == true) return
        quotesJob = viewModelScope.launch {
            getStockQuotesUseCase().collect { quotes ->
                updateState {
                    it.copy(
                        isLoading = false,
                        quotes = quotes
                    )
                }
            }
        }
    }

    private fun stopCollecting() {
        quotesJob?.cancel()
        quotesJob = null
    }

    private inline fun StocksScreenViewModel.updateState(
        update: (StocksScreenViewState) -> StocksScreenViewState
    ) {
        viewStateFlow.value = update(viewStateFlow.value)
    }
}
