package net.zero.three.api.payload

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Resource<T>(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("data")
    val data: T? = null,

    @SerializedName("message")
    val message: String,

    @SerializedName("code")
    val code: String? = ""
): Serializable