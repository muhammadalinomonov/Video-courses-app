package uz.gita.videocoursesapp.presentation.screen.pages.purchased

import org.orbitmvi.orbit.ContainerHost
import uz.gita.videocoursesapp.data.source.local.room.entity.LessonData

interface PurchasedContract {
    sealed interface Intent {
        object Load : Intent
        data class OpenDetailsScreen(val lessonData: LessonData) : Intent
    }

    sealed interface UiState {
        object Load : UiState
        data class Lessons(val list: List<LessonData>) : UiState
    }

    sealed interface SideEffect

    interface ViewModel : ContainerHost<UiState, SideEffect> {
        fun onEventDispatcher(intent: Intent)
    }
    interface Directions{
        suspend fun openDetailsScreen(lessonData: LessonData)
    }
}