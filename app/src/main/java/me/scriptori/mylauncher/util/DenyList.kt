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

object DenyList {
    private var TAG: String = DenyList::class.java.simpleName

    var currentDeniedList = mutableListOf<String>()

    private const val JSON_FILE = "deny_packages.json"

    private val gson: Gson
        get() = GsonBuilder().setPrettyPrinting().create()

    fun readFromFile(context: Context): DenyListResponse = gson.fromJson(
        BufferedReader(FileReader(getDenyListFile(context))),
        DenyListResponse::class.java
    )

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

    fun getDenyListFile(context: Context): File {
        val dataDir = Paths.get(context.dataDir.absolutePath).toFile()
        return File(dataDir, JSON_FILE)
    }

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