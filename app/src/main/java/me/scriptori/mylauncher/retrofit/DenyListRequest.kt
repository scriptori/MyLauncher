package me.scriptori.mylauncher.retrofit

import android.util.Log
import me.scriptori.mylauncher.model.DenyListViewModel
import me.scriptori.mylauncher.util.DenyPackageHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Class responsible for the asynchronous HTTP request using Retrofit
 */
class DenyListRequest(private val denyListViewModel: DenyListViewModel) {
    /**
     * Retrieve the deny list from the server URL asynchronously and populate the value of the
     * view model if success. In case of the failure, it will populate the view model value with
     * an empty list.
     */
    fun getDenyList() {
        // Implementation of the DenyListService
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(DenyListService::class.java)
        // Asynchronous HTTP request class
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
                } else {
                    handleFailure()
                }
            }

            override fun onFailure(call: Call<DenyListResponse>,  t: Throwable) {
                Log.d(TAG, "The HTTP request failed: $t")
                handleFailure()
            }
        })
    }

    private fun handleFailure() {
        Log.d(TAG, "Handling HTTP request failure")
        // Apply the default deny package list from the internal json file
        denyListViewModel.denyListResponse.value = DenyPackageHandler.defaultDenyResponse()
    }

    /**
     * Interface of the HTTP request API
     */
    interface DenyListService {
        /**
         * The relative URL of the resource for the server [URL]
         */
        @GET("b/61575fdf4a82881d6c5923a5")
        fun getDenyList(): Call<DenyListResponse>
    }

    companion object {
        private var TAG = DenyListRequest::class.java.simpleName
        /**
         * The server URL
         */
        private const val URL = "https://api.jsonbin.io/"
    }
}