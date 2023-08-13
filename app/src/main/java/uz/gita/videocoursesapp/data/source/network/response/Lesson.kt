package uz.gita.videocoursesapp.data.source.network.response

import com.google.gson.annotations.SerializedName
import uz.gita.videocoursesapp.data.source.local.room.entity.LessonData

data class Lesson(
    val description: String,
    val id: Int,
    val name: String,
    val thumbnail: String,
    @SerializedName("video_url")
    val videoUrl: String
) {
    fun toLessonEntity(isPurchased: Boolean = false) =
        LessonData(description, id, name, thumbnail, videoUrl, isPurchased = isPurchased)
}