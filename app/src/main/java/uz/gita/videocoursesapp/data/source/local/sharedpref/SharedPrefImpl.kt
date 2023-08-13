package uz.gita.videocoursesapp.data.source.local.sharedpref

import android.content.SharedPreferences
import javax.inject.Inject

class SharedPrefImpl @Inject constructor(private val pref: SharedPreferences) : SharedPref {
    override var firstThreeIndex: Int
        get() = pref.getInt("firstThreeIndex", 0)
        set(value) {
            pref.edit().putInt("firstThreeIndex", value).apply()
        }
}