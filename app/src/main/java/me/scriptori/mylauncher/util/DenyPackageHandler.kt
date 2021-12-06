package me.scriptori.mylauncher.util

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import me.scriptori.mylauncher.retrofit.DenyListResponse
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.OutputStreamWriter
import java.nio.file.Paths

/**
 * The deny package handler object is an utility object class that handles the I/O of the deny
 * package list defined in a json format.
 */
object DenyPackageHandler {
    private var TAG: String = DenyPackageHandler::class.java.simpleName

    var currentDeniedList = mutableListOf<String>()

    private const val JSON_FILE = "deny_packages.json"
    private const val DEFAULT_DENY_PACKAGES =
        "{\"denylist\": [" +
                "\"com.android.chrome\"," +
                "\"com.google.android.apps.maps\"," +
                "\"com.android.dialer\"," +
                "\"com.google.android.gm\"]}"

    private val gson: Gson
        get() = GsonBuilder().setPrettyPrinting().create()

    /**
     * Retrieves the default deny list response model with the default deny list stored in the
     * application assets json file. Notice that this is used only in the case there is no online
     * connection available and the deny list from the json file has not being generated yet.
     *
     * @return The DenyListResponse model class for the default deny list
     * @see [me.scriptori.mylauncher.retrofit.DenyListResponse]
     */
    fun defaultDenyResponse(): DenyListResponse = gson.fromJson(
        DEFAULT_DENY_PACKAGES,
        DenyListResponse::class.java
    )

    /**
     * Retrieves the deny list response model stored in the device user data folder of
     * the application.
     *
     * @param context - The application context
     *
     * @return The The DenyListResponse model class for the device saved json file
     * @see [me.scriptori.mylauncher.retrofit.DenyListResponse] class
     *
     */
    fun readFromFile(context: Context): DenyListResponse = gson.fromJson(
        BufferedReader(FileReader(getDenyListFile(context))),
        DenyListResponse::class.java
    )

    /**
     * Writes the deny list json file in the device user data folder of the application
     *
     * @param context - The application context
     */
    fun writeToFile(context: Context, input: DenyListResponse) {
        getDenyListFile(context).also { file ->
            try {
                file.createNewFile()
                val fOut = FileOutputStream(file)
                OutputStreamWriter(fOut).append(gson.toJson(input)).close()
                fOut.flush()
                fOut.close()
            } catch (e: Throwable) {
                Log.e(TAG, "Writing the ${file.absoluteFile} failed: $e")
            }
        }
    }

    /**
     * Retrieves the absolute path of the deny list json file from the device
     *
     * @param context - The application context
     *
     * @return  The file reference
     * @see [java.io.File]
     */
    fun getDenyListFile(context: Context): File {
        val dataDir = Paths.get(context.dataDir.absolutePath).toFile()
        return File(dataDir, JSON_FILE)
    }

    /**
     * Deletes the deny list json file from device
     *
     * @param context - The application context
     */
    fun deleteDenyListFile(context: Context) {
        getDenyListFile(context).also { file ->
            try {
                if (file.exists()) file.delete()
            } catch (e: Throwable) {
                Log.e(TAG, "The ${file.absoluteFile} couldn't be deleted failed: $e")
            }
        }
    }
}