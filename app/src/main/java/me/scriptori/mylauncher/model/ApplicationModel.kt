package me.scriptori.mylauncher.model

import android.graphics.drawable.Drawable

data class ApplicationModel(
    val label: String,
    val packageName: String,
    val icon: Drawable
)
