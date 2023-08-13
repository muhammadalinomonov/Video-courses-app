package uz.gita.videocoursesapp.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import uz.gita.videocoursesapp.data.source.local.room.entity.LessonData

@Dao
interface LessonDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLessons(lessonEntity: LessonData)

    @Query("SELECT * FROM LESSONS")
    suspend fun getAllLessons(): List<LessonData>

    @Update
    suspend fun update(lessonEntity: LessonData)

    @Query("SELECT * FROM LESSONS WHERE isPurchased = 1")
    suspend fun getPurchasedLessons(): List<LessonData>
}