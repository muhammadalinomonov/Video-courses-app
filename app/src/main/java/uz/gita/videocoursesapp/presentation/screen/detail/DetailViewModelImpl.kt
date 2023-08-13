package uz.gita.videocoursesapp.presentation.screen.detail

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
import uz.gita.videocoursesapp.navigation.AppNavigator
import javax.inject.Inject

@HiltViewModel
class DetailViewModelImpl @Inject constructor(
    private val repository: AppRepository,
    private val navigator: AppNavigator
) : DetailsContract.ViewModel, ViewModel() {


    override fun onEventDispatcher(intent: DetailsContract.Intent) {
        when (intent) {
            is DetailsContract.Intent.ChangeVideo -> {
                viewModelScope.launch {
                    navigator.replace(DetailScreen(intent.lessonData))
                }
            }

            is DetailsContract.Intent.Load -> {
                repository.getPurchasedCourses().onEach {
                    intent {
                        reduce {
                            DetailsContract.UiState.Lessons(
                                it.shuffled().filter { it != intent.lessonData })
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    override val container =
        container<DetailsContract.UiState, DetailsContract.SideEffect>(DetailsContract.UiState.Load)


}