package com.example.wsamad5.data

import com.example.wsamad5.core.Constants
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


fun signIn(email:String,password:String):RequestBody{
    val json = JSONObject().apply {
        put("login",email)
        put("password",password)
    }
    return json.toString().toRequestBody("application/json".toMediaType())
}

fun get(endpoint: String):Request{
    return Request.Builder().url("${Constants.URL}$endpoint").get().build()
}

fun post(endpoint: String,body: RequestBody):Request{
    return Request.Builder().url("${Constants.URL}$endpoint").post(body).build()
}