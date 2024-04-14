package com.bendeng.data.config

import com.bendeng.data.BuildConfig
import com.bendeng.data.remote.UnplashApi.Companion.AUTHORIZATION
import com.bendeng.data.remote.UnplashApi.Companion.CLIENT_ID
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import kotlin.jvm.Throws

class AccessTokenInterceptor @Inject constructor() : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val clientId = BuildConfig.UNPLASH_ID
        val builder: Request.Builder = chain.request().newBuilder()
        clientId.let {
            builder.addHeader(AUTHORIZATION, "$CLIENT_ID $it")
        }
        return chain.proceed(builder.build())
    }
}