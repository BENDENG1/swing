package com.bendeng.domain.model.base


sealed class BaseState<out T> {
    data class Success<out T>(val data: T) : BaseState<T>()
    data class Error(val message: String) : BaseState<Nothing>()
}