package uz.gita.videocoursesapp.data.source.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "lessons")
data class LessonData(
    val description: String,
    @PrimaryKey
    val id: Int,
    val name: String,
    val thumbnail: String,
    val videoUrl: String,
    val isPurchased: Boolean = false,
    val isSaved: Boolean = false
):Serializable
