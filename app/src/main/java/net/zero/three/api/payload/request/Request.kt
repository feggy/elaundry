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
    @SerializedName("idUser") val idUser: String,
    @SerializedName("idMerchant") val idMerchant: String,
    @SerializedName("namaCucian") val namaCucian: String,
    @SerializedName("berat") val berat: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("amountSatuan") val amountSatuan: String,
    @SerializedName("fee") val fee: String,
    @SerializedName("catatan") val catatan: String,
    @SerializedName("idJenisCucian") val idJenisCucian: String
)

data class ReqStore(
    @SerializedName("nearest") var nearest: Boolean? = false,
    @SerializedName("lat") var lat: String? = "",
    @SerializedName("long") var long: String? = ""
)

data class ReqHistory(
    @SerializedName("noHp") var noHp: String?,
    @SerializedName("status") var status: String
)

data class ReqPayment(
    @SerializedName("payment_method") val payment_method : String,
    @SerializedName("amount") val amount : String,
    @SerializedName("fee") val fee : String,
    @SerializedName("orderId") val orderId : String,
    @SerializedName("nama") val nama : String,
    @SerializedName("email") val email : String,
    @SerializedName("noHp") val noHp : String
)

data class ReqBiaya(
    @SerializedName("idMerchant") val idMerchant : String,
    @SerializedName("fee") val fee : String,
    @SerializedName("harga") val harga : String,
    @SerializedName("idJenisCucian") val idJenisCucian : String
)

data class ReqWithdrawal(
    @SerializedName("noHp") val noHp : String,
    @SerializedName("withdrawl") val withdrawl : String,
    @SerializedName("noRek") val noRek : String,
    @SerializedName("bankRek") val bankRek : String,
    @SerializedName("namaRek") val namaRek : String
)