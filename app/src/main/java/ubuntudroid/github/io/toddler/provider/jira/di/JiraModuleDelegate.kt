package ubuntudroid.github.io.toddler.provider.jira.di

import android.content.Context
import android.preference.PreferenceManager
import android.util.Base64
import com.facebook.stetho.okhttp.StethoInterceptor
import com.github.aurae.retrofit.LoganSquareConverterFactory
import com.squareup.okhttp.Interceptor
import com.squareup.okhttp.OkHttpClient
import retrofit.Retrofit
import retrofit.RxJavaCallAdapterFactory
import ubuntudroid.github.io.toddler.R
import ubuntudroid.github.io.toddler.User
import ubuntudroid.github.io.toddler.provider.jira.JiraService

/**
 * Created by Sven Bendel.
 */
object JiraModuleDelegate {

    fun provideOkHttpClient(): OkHttpClient {
        val client = OkHttpClient()
        client.networkInterceptors().add(StethoInterceptor())
        return client
    }

    fun provideService(client: OkHttpClient, applicationContext: Context): JiraService {
        client.networkInterceptors().add(Interceptor {
            val credentials = User.getLogin(applicationContext) + ":" + User.getPassword(applicationContext)
            val basic = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)

            val originalRequest = it.request()

            // add authentication to request
            val authRequest = originalRequest.newBuilder()
                    .header("Authorization", basic)
                    .method(originalRequest.method(), originalRequest.body())
                    .build()

            it.proceed(authRequest)
        })
        client.networkInterceptors().add(StethoInterceptor())
        val serviceBuilder = Retrofit.Builder().client(client)

        // set endpoint for service
        val server = PreferenceManager.getDefaultSharedPreferences(applicationContext).getString("server", applicationContext.getString(R.string.pref_default_server))

        serviceBuilder.baseUrl(server)

        serviceBuilder.addConverterFactory(LoganSquareConverterFactory.create())
        serviceBuilder.addCallAdapterFactory(RxJavaCallAdapterFactory.create())

        val jiraService = serviceBuilder.build()

        return jiraService.create<JiraService>(JiraService::class.java)
    }

}