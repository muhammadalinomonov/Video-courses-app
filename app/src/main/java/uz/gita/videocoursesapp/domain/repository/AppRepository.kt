package uz.gita.videocoursesapp.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.gita.videocoursesapp.data.source.local.room.entity.LessonData

interface AppRepository {
    fun getAllCourses(search:String): Flow<Result<List<LessonData>>>
    fun getPurchasedCourses(): Flow<List<LessonData>>
    suspend fun addToPurchased(lessonData: LessonData)

}