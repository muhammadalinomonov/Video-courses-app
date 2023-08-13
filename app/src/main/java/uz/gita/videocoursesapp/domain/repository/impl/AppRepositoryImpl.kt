package uz.gita.videocoursesapp.domain.repository.impl

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import uz.gita.videocoursesapp.data.source.local.room.dao.LessonDao
import uz.gita.videocoursesapp.data.source.local.room.entity.LessonData
import uz.gita.videocoursesapp.data.source.local.sharedpref.SharedPref
import uz.gita.videocoursesapp.data.source.network.api.Api
import uz.gita.videocoursesapp.domain.repository.AppRepository
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val api: Api,
    private val gson: Gson,
    private val dao: LessonDao,
    private val sharedPref: SharedPref
) :
    AppRepository {

    override fun getAllCourses(search: String): Flow<Result<List<LessonData>>> =
        flow {
            val response = api.getAllCourses()
            response.body()?.lessons?.map {
                dao.insertLessons(it.toLessonEntity(false))
            }
            val resulList: List<LessonData> =
                dao.getAllLessons().filter { it.name.contains(search, ignoreCase = true) }
            emit(Result.success(resulList))
        }.flowOn(Dispatchers.IO).catch { emit(Result.failure(Throwable("Try again"))) }

    override fun getPurchasedCourses(): Flow<List<LessonData>> = flow {
        emit(dao.getPurchasedLessons())
    }.flowOn(Dispatchers.IO)

    override suspend fun addToPurchased(lessonData: LessonData) {
        dao.update(lessonData)
    }


}