package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import com.example.data.model.ChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object CohereClient {

    private const val TAG = "CohereClient"
    private const val PLACEHOLDER_KEY = "MY_COHERE_API_KEY"
    private const val ENDPOINT = "https://api.cohere.com/v2/chat"
    private const val MODEL = "command-r-08-2024"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun generateChat(
        systemPrompt: String,
        userMessage: String,
        history: List<ChatMessage> = emptyList()
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.COHERE_API_KEY
        if (apiKey.isBlank() || apiKey == PLACEHOLDER_KEY) {
            return@withContext Result.failure(Exception("COHERE_API_KEY no configurada"))
        }
        try {
            val messagesArray = JSONArray()

            // 1. System Prompt with strict grounding and persona
            messagesArray.put(JSONObject().apply {
                put("role", "system")
                put("content", systemPrompt)
            })

            // 2. Multi-turn conversation history (last 8 messages for context and memory)
            val recentHistory = history.takeLast(8)
            for (msg in recentHistory) {
                // Avoid duplicating the current user message if it was already added to history
                if (msg.text.trim() == userMessage.trim()) continue

                messagesArray.put(JSONObject().apply {
                    put("role", if (msg.isFromUser) "user" else "assistant")
                    put("content", msg.text)
                })
            }

            // 3. Current User message
            messagesArray.put(JSONObject().apply {
                put("role", "user")
                put("content", userMessage)
            })

            val jsonPayload = JSONObject().apply {
                put("model", MODEL)
                put("messages", messagesArray)
            }

            val requestBody = jsonPayload.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url(ENDPOINT)
                .addHeader("Authorization", "Bearer $apiKey")
                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .build()

            val response = okHttpClient.newCall(request).execute()
            val responseBody = response.body?.string()

            if (!response.isSuccessful || responseBody.isNullOrBlank()) {
                Log.e(TAG, "Cohere error code ${response.code}: $responseBody")
                return@withContext Result.failure(Exception("HTTP ${response.code}: $responseBody"))
            }

            val jsonResponse = JSONObject(responseBody)
            val messageObj = jsonResponse.optJSONObject("message")
            val contentArray = messageObj?.optJSONArray("content")
            val firstTextObj = contentArray?.optJSONObject(0)
            val replyText = firstTextObj?.optString("text")

            if (!replyText.isNullOrBlank()) {
                Result.success(replyText)
            } else {
                Result.failure(Exception("Empty text content in Cohere response"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Cohere request failed", e)
            Result.failure(e)
        }
    }
}
