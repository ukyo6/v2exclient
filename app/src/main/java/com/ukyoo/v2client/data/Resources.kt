package com.ukyoo.v2client.data

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
data class Resources<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Resources<T> {
            return Resources(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T? = null): Resources<T> {
            return Resources(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T? = null): Resources<T> {
            return Resources(Status.LOADING, data, null)
        }
    }
}
