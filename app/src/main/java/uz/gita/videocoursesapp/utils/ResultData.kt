package uz.gita.videocoursesapp.utils

sealed class ResultData<T> {
    data class Success<T>(val data: T) : ResultData<T>()

    data class Error<T>(val error: Throwable) : ResultData<T>()


    fun onSuccess(block: (T) -> Unit): ResultData<T> {
        if (this is Success) {
            block(this.data)
        }
        return this
    }

    fun onFailure(block: (Throwable) -> Unit): ResultData<T> {
        if (this is Error) {
            block(this.error)
        }
        return this
    }
}
