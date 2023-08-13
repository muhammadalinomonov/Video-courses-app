package uz.gita.videocoursesapp.utils

import com.google.gson.Gson
import retrofit2.Response
import uz.gita.videocoursesapp.data.model.MessageData

fun <T> Response<T>.responseToResultData(gson: Gson): ResultData<T> {
    val data = this
    if (data.isSuccessful) {
        return if (data.body() != null) {
            val body = data.body()!!
            ResultData.Success(body)
        } else {
            ResultData.Error(Throwable("Body null"))
        }
    }

    val messageData = gson.fromJson(errorBody()!!.string(), MessageData::class.java)

    return ResultData.Error(
        Throwable(messageData?.message ?: "")
    )
}