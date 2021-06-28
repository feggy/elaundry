package net.zero.three.api.payload.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ResLogin(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("email_verified_at") val email_verified_at: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String,
    @SerializedName("store_name") val store_name: String,
    @SerializedName("no_hp") val no_hp: String,
    @SerializedName("address") val address: String,
    @SerializedName("latitude") val latitude: String,
    @SerializedName("longitude") val longitude: String,
    @SerializedName("profile_photo_path") val profile_photo_path: String,
    @SerializedName("level") val level: String,
    @SerializedName("active") val active: String,
    @SerializedName("profile_photo_url") val profile_photo_url: String
) : Serializable

data class ResRegister(
    @SerializedName("name") val name: String,
    @SerializedName("no_hp") val no_hp: String,
    @SerializedName("address") val address: String,
    @SerializedName("email") val email: String,
    @SerializedName("latitude") val latitude: String,
    @SerializedName("longitude") val longitude: String,
    @SerializedName("level") val level: String,
    @SerializedName("password") val password: String,
    @SerializedName("store_name") val store_name: String,
    @SerializedName("id") val id: Int,
    @SerializedName("image_store") val image_store: String,
    @SerializedName("profile_photo_url") val profile_photo_url: String
)

data class ResDetailAkun(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("email_verified_at") val email_verified_at: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String,
    @SerializedName("store_name") val store_name: String,
    @SerializedName("no_hp") val no_hp: String,
    @SerializedName("address") val address: String,
    @SerializedName("latitude") val latitude: String,
    @SerializedName("longitude") val longitude: String,
    @SerializedName("profile_photo_path") val profile_photo_path: String,
    @SerializedName("level") val level: String,
    @SerializedName("active") val active: String,
    @SerializedName("profile_photo_url") val profile_photo_url: String,
    @SerializedName("image_store") val image_store: String
)

data class ResOrder(
    @SerializedName("id_merchant") val  id_merchant: String,
    @SerializedName("id_user") val id_user: String,
    @SerializedName("nama_cucian") val  nama_cucian: String,
    @SerializedName("berat") val  berat: String,
    @SerializedName("amount_satuan") val  amount_satuan: String,
    @SerializedName("id") val  id: String,
    @SerializedName("created_at") val  created_at: String,
    @SerializedName("updated_at") val  updated_at: String
)

data class ResStore(
    @SerializedName("id") val id : Int,
    @SerializedName("store_name") val store_name : String?,
    @SerializedName("address") val address : String?,
    @SerializedName("latitude") val latitude : String,
    @SerializedName("longitude") val longitude : String,
    @SerializedName("image_store") val image_store : String,
    @SerializedName("distance") val distance : String,
    @SerializedName("profile_photo_url") val profile_photo_url : String,
    @SerializedName("no_hp") val no_hp : String
): Serializable

data class ResHistory(
    @SerializedName("id") val id : String,
    @SerializedName("id_user") val id_user : String,
    @SerializedName("id_merchant") val id_merchant : String,
    @SerializedName("nama_cucian") val nama_cucian : String,
    @SerializedName("berat") val berat : String,
    @SerializedName("amount_satuan") val amount_satuan : String,
    @SerializedName("amount") val amount : String,
    @SerializedName("status") val status : String,
    @SerializedName("status_pembayaran") val status_pembayaran : String,
    @SerializedName("status_pengerjaan") val status_pengerjaan : String,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("updated_at") val updated_at : String,
    @SerializedName("fee") val fee : String,
    @SerializedName("catatan") val catatan : String,
    @SerializedName("store_name") val store_name : String
)

data class ResPayment(
    @SerializedName("id_order") val id_order : String,
    @SerializedName("id_invoice") val id_invoice : String,
    @SerializedName("amount") val amount : String,
    @SerializedName("payment_method") val payment_method : String,
    @SerializedName("status") val status : String,
    @SerializedName("expired_time") val expired_time : String,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("updated_at") val updated_at : String,
    @SerializedName("reference") val reference : String,
    @SerializedName("pay_code") val pay_code : String,
    @SerializedName("id") val id : String
)