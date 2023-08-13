package uz.gita.videocoursesapp.presentation.screen.pages.purchased

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.gita.videocoursesapp.domain.repository.AppRepository
import javax.inject.Inject

@HiltViewModel
class PurchasedViewModelImpl @Inject constructor(
    private val repository: AppRepository,
    private val directions: PurchasedContract.Directions
) :
    PurchasedContract.ViewModel, ViewModel() {
    override val container =
        container<PurchasedContract.UiState, PurchasedContract.SideEffect>(PurchasedContract.UiState.Load)

    init {
        onEventDispatcher(PurchasedContract.Intent.Load)
    }
    override fun onEventDispatcher(intent: PurchasedContract.Intent) {
        when (intent) {
            PurchasedContract.Intent.Load -> {
                repository.getPurchasedCourses().onEach {
                    intent{
                        reduce {

                            PurchasedContract.UiState.Lessons(it)
                        }
                    }
                }.launchIn(viewModelScope)
            }

            is PurchasedContract.Intent.OpenDetailsScreen -> {
                viewModelScope.launch {
                    directions.openDetailsScreen(intent.lessonData)
                }
            }
        }
    }
}