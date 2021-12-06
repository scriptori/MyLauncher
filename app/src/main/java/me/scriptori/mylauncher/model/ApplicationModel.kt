package me.scriptori.mylauncher.model

import android.graphics.drawable.Drawable

/**
 * The data model for the application info.
 * This data model will b e used by the recyclerview items in the application drawer
 */
data class ApplicationModel(
    val label: String,
    val packageName: String,
    val icon: Drawable
)
