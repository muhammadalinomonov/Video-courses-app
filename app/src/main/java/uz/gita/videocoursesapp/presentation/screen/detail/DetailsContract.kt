package uz.gita.videocoursesapp.presentation.screen.detail

import org.orbitmvi.orbit.ContainerHost
import uz.gita.videocoursesapp.data.source.local.room.entity.LessonData
interface DetailsContract {
    sealed interface Intent {
        data class Load(val lessonData: LessonData) : Intent
        data class ChangeVideo(val lessonData: LessonData):Intent
    }
    sealed interface UiState {
        object Load : UiState
        data class Lessons(val list: List<LessonData>) : UiState
    }

    sealed interface SideEffect

    interface ViewModel : ContainerHost<UiState, SideEffect> {
        fun onEventDispatcher(intent: Intent)
    }


}