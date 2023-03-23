package com.github.desfate.gptcore.http


import com.github.desfate.gptcore.config.BASE_SECRET_KEY
import com.github.desfate.gptcore.config.DEBUG_BASE_URL
import com.hjq.gson.factory.GsonFactory
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Http网络请求引擎
 */
class HttpEngine private constructor() {

    /**
     * 双重校验锁单例
     */
    companion object {
        var authorization = "";

        val instance: HttpEngine by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            HttpEngine()
        }
    }

    var okHttpClient: OkHttpClient? = null
    var retrofit: Retrofit? = null

    private val timeOut = 30000L  // 超时时间



    /**
     * http网络请求基于OkHttp + Retrofit
     */
    init {

        // 初始化http请求核心
        if (okHttpClient == null) {
            val logInterceptor = HttpLoggingInterceptor(LogInterceptor())    // 请求日志拦截器
            val netInterceptor = NetInterceptor()   //                          网络请求头部拦截器

            logInterceptor.level = HttpLoggingInterceptor.Level.BODY  //        设置日志输出等级
            okHttpClient = OkHttpClient.Builder()
                .addInterceptor(netInterceptor)
                .addInterceptor(logInterceptor)
//                .connectionSpecs(Collections.singletonList(spec))
                .retryOnConnectionFailure(false)
                    .connectTimeout(timeOut, TimeUnit.MILLISECONDS)
                .readTimeout(timeOut, TimeUnit.MILLISECONDS)
                .build()
        }

        // 初始化网络框架
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(DEBUG_BASE_URL) //BaseUrl
                .client(okHttpClient!!) //请求的网络框架
                .addConverterFactory(GsonConverterFactory.create(GsonFactory.getSingletonGson()))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // 使用RxJava作为回调适配器
                .build()
        }

    }

    /**
     * 发起请求
     */
    fun <T> create(service: Class<T>): T {
        return retrofit?.create(service)!!
    }

    /**
     * 打印消息
     */
    class LogInterceptor : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            println(message)
        }
    }


    class NetInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
            if (authorization.isEmpty()) authorization = BASE_SECRET_KEY
            val requestBuilder = request  // 自定义 header 处理
                .addHeader("Connection", "close")
                .addHeader("Accept-Encoding", "identity")
                .addHeader("Authorization", "Bearer $authorization")
                .build()
            return chain.proceed(requestBuilder)
        }
    }
}
