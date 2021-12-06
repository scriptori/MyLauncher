package me.scriptori.mylauncher.util

import android.content.Context
import android.content.Intent
import me.scriptori.mylauncher.MyLauncherActivity
import me.scriptori.mylauncher.model.ApplicationModel

/**
 * The allowed package handler object is an utility object class that handles the retrieval of the
 * package list defined in a json format.
 */
object AllowedPackageHandler {
    internal const val APP_PACKAGE_NAME = "me.scriptori.mylauncher"

    /**
     * Retrieves the list of [ApplicationModel] objects in alphabetic order to be displayed
     * in the launcher drawer view
     *
     * @param context - The application context
     *
     * @return The list of allowed application models
     * @see [me.scriptori.mylauncher.model.ApplicationModel]
     */
    internal fun getAllowedApps(context: Context): MutableList<ApplicationModel> {
        val appModelList = mutableListOf<ApplicationModel>()
        val activity = (context as MyLauncherActivity)
        val dpm = activity.dpm
        activity.packageManager.let { pm ->
            val intent = Intent(Intent.ACTION_MAIN, null).also {
                it.addCategory(Intent.CATEGORY_LAUNCHER)
            }
            pm.queryIntentActivities(intent, 0).forEach {
                val packageName = it.activityInfo.packageName
                if ( // Hiding denied application
                    !DenyPackageHandler.currentDeniedList.contains(packageName) &&
                    // Hiding system application
                    pm.getLaunchIntentForPackage(packageName) != null &&
                    // Hiding launcher application
                    packageName != APP_PACKAGE_NAME
                ) {
                    // adding application information to the available app list
                    appModelList.add(
                        ApplicationModel(
                            it.loadLabel(pm).toString(),
                            it.activityInfo.packageName,
                            it.activityInfo.loadIcon(pm)
                        )
                    )
                }
            }
        }
        // Sorting the list alphabetically
        appModelList.sortBy { it.label }
        if (dpm.isDeviceOwnerApp(activity.packageName)) {
            dpm.setLockTaskPackages(activity.adminComponentName, appModelList.map {
                it.packageName
            }.toTypedArray())
        }
        return appModelList
    }
}