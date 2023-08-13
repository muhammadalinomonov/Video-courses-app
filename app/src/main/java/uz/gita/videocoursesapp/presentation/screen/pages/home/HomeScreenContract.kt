package uz.gita.videocoursesapp.presentation.screen.pages.home

import android.content.Context
import org.orbitmvi.orbit.ContainerHost
import uz.gita.videocoursesapp.data.source.local.room.entity.LessonData

interface HomeScreenContract {
    sealed interface Intent {
        data class Load(val search:String, val context:Context) : Intent
        data class OpenDetailScreen(val lessonData: LessonData) : Intent
        data class PurchaseLesson(val lesson:LessonData):Intent
    }

    sealed interface UiState {
        data class Loading(val refreshState:Boolean, val index:Int) : UiState
        data class Courses(val list: List<LessonData>, val isRefresh:Boolean = true, val index:Int) : UiState
        object HasNowConnection : UiState
    }

    sealed interface SideEffect {
        data class HasError(val message: String) : SideEffect

    }

    interface ViewModel : ContainerHost<UiState, SideEffect> {
        fun onEventDispatcher(intent: Intent)
    }

    interface Directions {
        suspend fun openDetailsScreen(lessonData: LessonData)
    }
}