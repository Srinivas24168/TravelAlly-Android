package com.github.skrox.travelally.data.network

import com.github.skrox.travelally.R
import com.github.skrox.travelally.data.network.responses.AuthResponse
import com.github.skrox.travelally.data.network.postobjects.SendToken
import com.github.skrox.travelally.data.network.responses.TripsResponse
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface MyApi {

    @POST("user/login/")
    suspend fun userLogin(
        @Body id_token: SendToken?
    ) : Response<AuthResponse>


//    @Headers("Authorization: Token ${}")
    @GET("trips/popular/")
    suspend fun getPopularTrips(@HeaderMap headers:Map<String,String?>) : Response<TripsResponse>

    companion object{
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ) : MyApi{

            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl("http://192.168.1.110:8080/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }

}

