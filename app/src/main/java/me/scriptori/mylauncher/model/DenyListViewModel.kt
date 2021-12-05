package me.scriptori.mylauncher.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.scriptori.mylauncher.retrofit.DenyListResponse

class DenyListViewModel : ViewModel() {
    var denyListResponse = MutableLiveData(DenyListResponse())
}