package com.bendeng.data.config

import com.bendeng.data.BuildConfig
import com.bendeng.data.remote.UnplashApi.Companion.AUTHORIZATION
import com.bendeng.data.remote.UnplashApi.Companion.BEARER
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class AccessTokenInterceptor @Inject constructor() : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val clientToken = BuildConfig.UNPLASH_TOKEN
        val builder: Request.Builder = chain.request().newBuilder()
        clientToken.let {
            builder.addHeader(AUTHORIZATION, "$BEARER $it")
        }
        return chain.proceed(builder.build())
    }
}