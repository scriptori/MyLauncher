package me.scriptori.mylauncher.retrofit

import me.scriptori.mylauncher.model.DenyListViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class DenyListRequest(private val denyListViewModel: DenyListViewModel) {
    fun getDenyList() {
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(DenyListService::class.java)
        val call = service.getDenyList()
        call.enqueue(object : Callback<DenyListResponse> {
            override fun onResponse(
                call: Call<DenyListResponse>,
                denyListResponse: Response<DenyListResponse>
            ) {
                if (denyListResponse.code() == 200) {
                    denyListResponse.body()?.let { body ->
                        denyListViewModel.denyListResponse.value = body
                    }
                }
            }

            override fun onFailure(call: Call<DenyListResponse>,  t: Throwable) {
                denyListViewModel.denyListResponse.value = DenyListResponse()
            }
        })
    }

    interface DenyListService {
        @GET("b/61575fdf4a82881d6c5923a5")
        fun getDenyList(): Call<DenyListResponse>
    }

    companion object {
        private const val URL = "https://api.jsonbin.io/"
    }
}