package net.zero.three.api

import android.util.Log
import com.google.gson.GsonBuilder
import net.zero.three.BuildConfig
import net.zero.three.api.private_interface.AuthInterface
import net.zero.three.constants.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitFactory {

    fun <T> makeRetrofitService(serviceType: Class<T>) : T{
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(makeOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(serviceType)
    }

    private fun makeOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(makeLoggingInterceptor())
//            .addInterceptor(checkSessionInterceptor())
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("accesskey", Constants.ACCESS_KEY)
                    .build()
                chain.proceed(request)
            }
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .build()
    }

    private fun makeLoggingInterceptor(): HttpLoggingInterceptor {
        val log = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Log.e("OkHttp", it)
        })
        log.level = HttpLoggingInterceptor.Level.BODY
        return log
    }

    private fun checkSessionInterceptor(): HttpLoggingInterceptor {
        val log = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            try {
                val jsonObj = JSONObject(it)
                if (!jsonObj.getBoolean("status")) {
                    val message = jsonObj.getString("message")
                }
            } catch (ex: Exception){}
            Log.e("OKHTTP", it)
        })
        log.level = HttpLoggingInterceptor.Level.BODY
        return log
    }

    open class Service {
        companion object {
            val auth: AuthInterface by lazy {
                makeRetrofitService(AuthInterface::class.java)
            }
        }
    }
}