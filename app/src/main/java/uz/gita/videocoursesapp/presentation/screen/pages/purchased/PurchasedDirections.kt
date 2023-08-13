package uz.gita.videocoursesapp.presentation.screen.pages.purchased

import uz.gita.videocoursesapp.data.source.local.room.entity.LessonData
import uz.gita.videocoursesapp.navigation.AppNavigator
import uz.gita.videocoursesapp.presentation.screen.detail.DetailScreen
import javax.inject.Inject

class PurchasedDirections @Inject constructor(private val navigator: AppNavigator) :
    PurchasedContract.Directions {
    override suspend fun openDetailsScreen(lessonData: LessonData) {
        navigator.navigateTo(DetailScreen(lessonData))
    }
}