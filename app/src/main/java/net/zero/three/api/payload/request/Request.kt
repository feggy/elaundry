package net.zero.three.api.payload.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReqLogin(
    @SerializedName("no_hp")
    val no_hp: String,
    @SerializedName("password")
    val password: String
)

data class ReqRegister(
    @SerializedName("nama") val nama : String,
    @SerializedName("noHp") val noHp : String,
    @SerializedName("alamat") val alamat : String,
    @SerializedName("email") val email : String,
    @SerializedName("lat") val lat : String,
    @SerializedName("long") val long : String,
    @SerializedName("password") val password : String,
    @SerializedName("level") val level : String = "user"
)