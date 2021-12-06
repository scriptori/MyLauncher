package me.scriptori.mylauncher.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.scriptori.mylauncher.retrofit.DenyListResponse

/**
 * The view model live data for the denylist in DenyListResponse model.
 */
class DenyListViewModel : ViewModel() {
    var denyListResponse = MutableLiveData(DenyListResponse())
}