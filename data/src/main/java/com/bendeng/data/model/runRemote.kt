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
            } ?: BaseState.Error("검색 결과 존재하지 않습니다.")
        } else {
            BaseState.Error(HttpStatus.getMessageForCode(response.code()))
        }
    } catch (e: Exception) {
        BaseState.Error(e.message.toString())
    }
}

enum class HttpStatus(val code: Int, val message: String) {
    BAD_REQUEST(400, "요청이 정확하지 않습니다.\n다시 확인하세요."),
    UNAUTHORIZED(401, "현재 사용자는 인가되지 않은 사용자 입니다\n"),
    FORBIDDEN(403, "요청의 횟수가 허용된 갯수를 초과하였습니다.\n나중에 시도하세요."),
    NOT_FOUND(404, "찾을 수 없습니다.\n"),
    INTERNAL_SERVER_ERROR(500, "서버의 문제가 생겼습니다.\n"),
    SERVICE_UNAVAILABLE(503, "현재 서비스를 사용할 수 없습니다.\n");

    companion object {
        fun getMessageForCode(code: Int): String {
            return values().find { it.code == code }?.message ?: "알수 없는 에러 발생"
        }
    }
}
