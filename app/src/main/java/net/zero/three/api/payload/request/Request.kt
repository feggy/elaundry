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
    @SerializedName("level") val level : String,
    @SerializedName("imageStore") val imageStore: String,
    @SerializedName("storeName") val storeName: String
)

data class ReqOrder(
    @SerializedName("idUser") val idUser: Int,
    @SerializedName("idMerchant") val idMerchant: Int,
    @SerializedName("namaCucian") val namaCucian: String,
    @SerializedName("berat") val berat: Int,
    @SerializedName("amount") val amount: Int,
    @SerializedName("fee") val fee: Int,
)

data class ReqStore(
    @SerializedName("nearest") var nearest: Boolean? = false,
    @SerializedName("lat") var lat: String? = "",
    @SerializedName("long") var long: String? = ""
)