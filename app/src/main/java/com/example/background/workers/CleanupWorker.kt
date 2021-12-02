package com.example.background.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.background.OUTPUT_PATH
import java.io.File


class CleanupWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    override fun doWork(): Result {
        makeStatusNotification("Cleaning up old temporary files", applicationContext)
        sleep() // for u only to see the steps, u should to remove it when everything is OK

        return try {
            val outputDirectory = File(applicationContext.filesDir, OUTPUT_PATH)
            if (outputDirectory.exists()) {
                val entries = outputDirectory.listFiles()
                if (entries != null) {
                    for (entry in entries) {
                        val name = entry.name
                        if (name.isNotEmpty() && name.endsWith(".png")) {
                            val deleted = entry.delete()
                            Log.i("TAG", "Deleted $name - $deleted")
                        }
                    }
                }
            }
            Result.success() // return success as Result
        } catch (exception: Exception) {
            exception.printStackTrace()
            Result.failure() // return failed as Result
        }
    }
}  // end CleanupWorker
