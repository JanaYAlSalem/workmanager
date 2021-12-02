package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.background.KEY_IMAGE_URI

class BlurWorker(ctx: Context,
                 params: WorkerParameters) : Worker(ctx, params) {

    override fun doWork(): Result {

        val appContext = applicationContext

        val resourceUri = inputData.getString(KEY_IMAGE_URI) // Update BlurWorker's doWork() to get the inputs

        makeStatusNotification("Blurring image", appContext)

        sleep() // for u only to see the steps, u should to remove it when everything is OK

        return try {
//            val picture = BitmapFactory.decodeResource(appContext.resources, R.drawable.android_cupcake)

            if (TextUtils.isEmpty(resourceUri)) {
                Log.e("TAG", "Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }

            val resolver = appContext.contentResolver


//  `openInputStream(Uri.parse(resourceUri)))`
            val picture = BitmapFactory.decodeStream(resolver.openInputStream(Uri.parse(resourceUri)))
            val output = blurBitmap(picture, appContext)


            val outputUri = writeBitmapToFile(appContext, output) // Write bitmap to a temp file
            val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())

//            makeStatusNotification("Output is $outputUri", appContext)

            Result.success(outputData) // return success as Result

        } catch (throwable: Throwable) {
            Log.e("TAG", "Error applying blur")
            throwable.printStackTrace()
            Result.failure() // return failure as Result
        }
    } // end doWork() override fun


} // end BlurWorker class
