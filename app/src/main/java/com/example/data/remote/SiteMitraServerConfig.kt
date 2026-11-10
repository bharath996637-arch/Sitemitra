package com.example.data.remote

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

object SiteMitraServerConfig {

    // Configured Google Cloud Server URL provided for SiteMitra
    const val GOOGLE_SERVER_BASE_URL = "https://ais-pre-teslros233v7olr72hky2x-119054699939.asia-northeast1.run.app/"
    const val GOOGLE_SERVER_WEB_URL = "https://ais-pre-teslros233v7olr72hky2x-119054699939.asia-northeast1.run.app"
    const val GOOGLE_DEV_SERVER_URL = "https://ais-dev-teslros233v7olr72hky2x-119054699939.asia-northeast1.run.app"
    const val GOOGLE_SERVER_SHORT_HOST = "ais-pre-teslros233v7olr72hky2x-119054699939.asia-northeast1.run.app"
    const val SERVER_REGION = "Google Cloud Run (asia-northeast1)"

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(8, TimeUnit.SECONDS)
        .readTimeout(8, TimeUnit.SECONDS)
        .build()

    /**
     * Checks connection to the Google Cloud Run server and measures latency
     */
    suspend fun pingGoogleServer(): ServerPingResult = withContext(Dispatchers.IO) {
        val startTime = System.currentTimeMillis()
        try {
            val primaryRequest = Request.Builder()
                .url(GOOGLE_SERVER_WEB_URL)
                .header("User-Agent", "SiteMitra-Android-Client/1.0")
                .head()
                .build()

            var response = httpClient.newCall(primaryRequest).execute()
            if (!response.isSuccessful && response.code == 404) {
                response.close()
                val devRequest = Request.Builder()
                    .url(GOOGLE_DEV_SERVER_URL)
                    .header("User-Agent", "SiteMitra-Android-Client/1.0")
                    .head()
                    .build()
                response = httpClient.newCall(devRequest).execute()
            }

            response.use { res ->
                val latency = System.currentTimeMillis() - startTime
                ServerPingResult(
                    isSuccess = res.isSuccessful || res.code in 200..399,
                    statusCode = res.code,
                    latencyMs = latency,
                    message = "Connected to Google Cloud Server (${res.code} OK)"
                )
            }
        } catch (e: Exception) {
            val latency = System.currentTimeMillis() - startTime
            ServerPingResult(
                isSuccess = false,
                statusCode = 0,
                latencyMs = latency,
                message = "Connection issue: ${e.localizedMessage ?: "Network error"}"
            )
        }
    }

    /**
     * Opens the Google Cloud server web portal in the browser
     */
    fun openWebPortal(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_SERVER_WEB_URL)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Cannot open browser: $GOOGLE_SERVER_WEB_URL", Toast.LENGTH_LONG).show()
        }
    }
}

data class ServerPingResult(
    val isSuccess: Boolean,
    val statusCode: Int,
    val latencyMs: Long,
    val message: String
)
