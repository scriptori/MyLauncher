package me.scriptori.mylauncher.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.scriptori.mylauncher.retrofit.DenyListResponse

/**
 * The view model live data for the denylist in DenyListResponse model.
 */
class DenyListViewModel : ViewModel() {
    private var _denyListResponse = MutableLiveData(DenyListResponse())
    val denyListResponse: LiveData<DenyListResponse> = _denyListResponse

    fun updateDenyListResponse(response: DenyListResponse) {
        _denyListResponse.value = response
    }
}
