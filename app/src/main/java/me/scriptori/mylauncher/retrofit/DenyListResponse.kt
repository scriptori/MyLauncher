package me.scriptori.mylauncher.retrofit

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import me.scriptori.mylauncher.util.Constants
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.OutputStreamWriter
import java.nio.file.Paths

class DenyListResponse(val denylist: MutableList<String> = mutableListOf()) {
    companion object {
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
                    Log.e(Constants.TAG, "Writing the ${file.absoluteFile} failed: $e")
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
                    Log.e(Constants.TAG, "The ${file.absoluteFile} couldn't be deleted failed: $e")
                }
            }
        }
    }
}