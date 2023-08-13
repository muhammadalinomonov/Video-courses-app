package uz.gita.videocoursesapp.data.source.local.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.gita.videocoursesapp.data.source.local.room.dao.LessonDao
import uz.gita.videocoursesapp.data.source.local.room.entity.LessonData

@Database(entities = [LessonData::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getFoodsDao(): LessonDao

}