package com.example.data.remote

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

object SiteMitraServerConfig {

    // Configured Google Cloud Server URL provided for SiteMitra
    const val GOOGLE_SERVER_BASE_URL = "https://ais-pre-teslros233v7olr72hky2x-119054699939.asia-northeast1.run.app/"
    const val GOOGLE_SERVER_WEB_URL = "https://ais-pre-teslros233v7olr72hky2x-119054699939.asia-northeast1.run.app"
    const val GOOGLE_DEV_SERVER_URL = "https://ais-dev-teslros233v7olr72hky2x-119054699939.asia-northeast1.run.app"
    const val LOCAL_EMULATOR_SERVER_URL = "http://10.0.2.2:3000/"
    const val GOOGLE_SERVER_SHORT_HOST = "ais-pre-teslros233v7olr72hky2x-119054699939.asia-northeast1.run.app"
    const val SERVER_REGION = "Google Cloud Run (asia-northeast1)"

    @Volatile
    var activeBaseUrl: String = GOOGLE_SERVER_BASE_URL
        private set

    private val candidateBaseUrls = listOf(
        GOOGLE_SERVER_BASE_URL,
        "$GOOGLE_DEV_SERVER_URL/",
        LOCAL_EMULATOR_SERVER_URL
    )

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(8, TimeUnit.SECONDS)
        .writeTimeout(8, TimeUnit.SECONDS)
        .build()

    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val apiCache = ConcurrentHashMap<String, SiteMitraApi>()

    fun getApi(baseUrl: String = activeBaseUrl): SiteMitraApi {
        val normalized = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"
        return apiCache.getOrPut(normalized) {
            Retrofit.Builder()
                .baseUrl(normalized)
                .client(httpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(SiteMitraApi::class.java)
        }
    }

    /**
     * Probes the primary Cloud Run server, dev server, and local emulator fallback (10.0.2.2:3000)
     * and returns a live SiteMitraApi client bound to the first reachable Data Server.
     */
    suspend fun resolveReachableApi(): SiteMitraApi? = withContext(Dispatchers.IO) {
        val orderedCandidates = buildList {
            add(activeBaseUrl)
            candidateBaseUrls.forEach { if (it != activeBaseUrl) add(it) }
        }
        for (baseUrl in orderedCandidates) {
            try {
                val api = getApi(baseUrl)
                val health = api.getHealth()
                if (health.status.equals("ok", ignoreCase = true)) {
                    activeBaseUrl = baseUrl
                    return@withContext api
                }
            } catch (_: Exception) {
                // Probe next endpoint
            }
        }
        null
    }

    /**
     * Checks live connection to the SiteMitra Data Server /api/health endpoint and measures latency
     */
    suspend fun pingGoogleServer(): ServerPingResult = withContext(Dispatchers.IO) {
        val startTime = System.currentTimeMillis()
        val orderedCandidates = buildList {
            add(activeBaseUrl)
            candidateBaseUrls.forEach { if (it != activeBaseUrl) add(it) }
        }

        var lastError: String? = null
        for (baseUrl in orderedCandidates) {
            try {
                val healthUrl = "${baseUrl.trimEnd('/')}/api/health"
                val request = Request.Builder()
                    .url(healthUrl)
                    .header("User-Agent", "SiteMitra-Android-Client/1.0")
                    .get()
                    .build()

                httpClient.newCall(request).execute().use { res ->
                    val contentType = res.header("Content-Type").orEmpty()
                    if (res.isSuccessful && contentType.contains("application/json", ignoreCase = true)) {
                        val bodyStr = res.body?.string().orEmpty()
                        val health = moshi.adapter(HealthResponseDto::class.java).fromJson(bodyStr)
                        if (health != null && health.status.equals("ok", ignoreCase = true)) {
                            activeBaseUrl = baseUrl
                            val latency = System.currentTimeMillis() - startTime
                            val endpointLabel = if (baseUrl == LOCAL_EMULATOR_SERVER_URL) {
                                "Container Data Server (10.0.2.2:3000)"
                            } else {
                                "Google Cloud Data Server"
                            }
                            return@withContext ServerPingResult(
                                isSuccess = true,
                                statusCode = res.code,
                                latencyMs = latency,
                                message = "Connected to $endpointLabel (${health.counts.jobs} jobs, ${health.counts.sites} sites)",
                                activeUrl = baseUrl
                            )
                        }
                    } else {
                        lastError = "HTTP ${res.code} at $baseUrl"
                    }
                }
            } catch (e: Exception) {
                lastError = e.localizedMessage ?: "Network error"
            }
        }

        val latency = System.currentTimeMillis() - startTime
        ServerPingResult(
            isSuccess = false,
            statusCode = 0,
            latencyMs = latency,
            message = "Offline mode (local Room DB active)${lastError?.let { ": $it" } ?: ""}",
            activeUrl = activeBaseUrl
        )
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
    val message: String,
    val activeUrl: String = SiteMitraServerConfig.GOOGLE_SERVER_BASE_URL
)
