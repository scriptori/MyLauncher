package me.scriptori.mylauncher.retrofit

/**
 * The deny package list data model to be consumed by the json data
 */
data class DenyListResponse(
    val denylist: MutableList<String> = mutableListOf()
)
