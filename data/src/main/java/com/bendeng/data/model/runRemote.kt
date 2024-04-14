package com.bendeng.data.model

import com.bendeng.domain.model.base.BaseState
import com.google.gson.Gson
import retrofit2.Response

suspend fun <T> runRemote(block: suspend () -> Response<T>): BaseState<T> {
    return try {
        val response = block()
        if (response.isSuccessful) {
            response.body()?.let {
                BaseState.Success(it)
            } ?: BaseState.Error("통신이 정상적으로 진행되지 않았습니다.")
        } else {
            val errorData =
                Gson().fromJson(response.errorBody()?.string(), BaseState.Error::class.java)
            BaseState.Error(errorData.message)
        }
    } catch (e: Exception) {
        BaseState.Error(e.message.toString())
    }
}
