package uz.gita.videocoursesapp.presentation.screen.pages.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.gita.videocoursesapp.data.source.local.sharedpref.SharedPref
import uz.gita.videocoursesapp.domain.repository.AppRepository
import uz.gita.videocoursesapp.utils.hasConnectedNetwork
import javax.inject.Inject

@HiltViewModel
class HomeViewModelImpl @Inject constructor(
    private val directions: HomeScreenContract.Directions,
    private val repository: AppRepository,
    private val sharedpref: SharedPref
) : ViewModel(), HomeScreenContract.ViewModel {
    override val container =
        container<HomeScreenContract.UiState, HomeScreenContract.SideEffect>(
            HomeScreenContract.UiState.Loading(
                true,
                sharedpref.firstThreeIndex
            )
        )

    override fun onEventDispatcher(intent: HomeScreenContract.Intent) {
        when (intent) {
            is HomeScreenContract.Intent.Load -> {
                if (intent.context.hasConnectedNetwork()) {
                    repository.getAllCourses(intent.search).onEach {
                        it.onSuccess {
                            intent {
                                reduce {
                                    HomeScreenContract.UiState.Courses(
                                        it,
                                        false,
                                        sharedpref.firstThreeIndex
                                    )
                                }
                            }
                        }
                        it.onFailure {
                            intent {
                                reduce {
                                    HomeScreenContract.UiState.Loading(
                                        false,
                                        sharedpref.firstThreeIndex
                                    )
                                }
                            }
                            intent { postSideEffect(HomeScreenContract.SideEffect.HasError(it.message!!)) }
                        }
                    }.launchIn(viewModelScope)
                } else {
                    intent {
                        reduce { HomeScreenContract.UiState.HasNowConnection }
                    }
                }
            }

            is HomeScreenContract.Intent.OpenDetailScreen -> {
                viewModelScope.launch {
                    directions.openDetailsScreen(intent.lessonData)
                }
            }

            is HomeScreenContract.Intent.PurchaseLesson -> {
                sharedpref.firstThreeIndex++
                viewModelScope.launch {
                    repository.addToPurchased(intent.lesson.copy(isPurchased = true))
                }
            }
        }
    }
}