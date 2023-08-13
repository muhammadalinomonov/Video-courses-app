package uz.gita.videocoursesapp.data.source.network.api

import retrofit2.Response
import retrofit2.http.GET
import uz.gita.videocoursesapp.data.source.network.response.AllCoursesResponse

interface Api {
    @GET("lessons")
    suspend fun getAllCourses(): Response<AllCoursesResponse>
}