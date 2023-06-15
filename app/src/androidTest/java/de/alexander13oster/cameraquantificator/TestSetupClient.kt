package de.alexander13oster.cameraquantificator

import com.google.gson.Gson
import de.alexander13oster.shared.model.Barcode
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

class TestSetupClient {
    var client: OkHttpClient = OkHttpClient()

    fun changeBrightness(brightness: Int): Response {
        val url: HttpUrl = HttpUrl.Builder()
            .scheme("http")
            .host(IP_LIGHT_CONTROLLER)
            .addPathSegment("brightness")
            .addQueryParameter("brightness", brightness.toString())
            .build()
        val request = Request.Builder()
            .method("GET", null)
            .url(url)
            .build()

        return client.newCall(request).execute()
    }

    fun changeBarcode(barcode: Barcode): Response {
        val url: HttpUrl = HttpUrl.Builder()
            .scheme("http")
            .host(IP_BARCODE_APP)
            .port(8080)
            .addPathSegment("barcode")
            .build()
        val body = barcode.toJsonString().toRequestBody(MEDIA_TYPE_JSON)
        val request = Request.Builder()
            .method("POST", body)
            .url(url)
            .build()

        return client.newCall(request).execute()
    }

    private fun Barcode.toJsonString() = Gson().toJson(this)

    companion object {
        private const val IP_LIGHT_CONTROLLER = "192.168.178.106"
        private const val IP_BARCODE_APP = "192.168.178.68"

        private val MEDIA_TYPE_JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
    }
}