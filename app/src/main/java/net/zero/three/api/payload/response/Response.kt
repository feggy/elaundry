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
    @SerializedName("image_store") val image_store: String,
    @SerializedName("saldo") val saldo: String,
    @SerializedName("fee") val fee: String,
    @SerializedName("laundry_per_kg") val laundry_per_kg: String
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
    @SerializedName("id") val id : String,
    @SerializedName("fee") val fee : String,
    @SerializedName("total_bayar") val total_bayar : String
)

data class ResDetailOrder(
    @SerializedName("id_order") var id_order : String,
    @SerializedName("id_invoice") var id_invoice : String,
    @SerializedName("created_at") var created_at : String,
    @SerializedName("status") var status : String,
    @SerializedName("nama_pelanggan") var nama_pelanggan : String,
    @SerializedName("hp_pelanggan") var hp_pelanggan : String,
    @SerializedName("email_pelanggan") var email_pelanggan : String,
    @SerializedName("alamat_pelanggan") var alamat_pelanggan : String,
    @SerializedName("store_name") var store_name : String,
    @SerializedName("nama_cucian") var nama_cucian : String,
    @SerializedName("hp_laundry") var hp_laundry : String,
    @SerializedName("alamat_laundry") var alamat_laundry : String,
    @SerializedName("berat") var berat : String,
    @SerializedName("catatan") var catatan : String,
    @SerializedName("payment_method") var payment_method : String,
    @SerializedName("pay_code") var pay_code : String,
    @SerializedName("sub_total") var sub_total : String,
    @SerializedName("biaya_layanan") var biaya_layanan : String,
    @SerializedName("total_bayar") var total_bayar : String
)

data class ResWithdrawalHistory(
    @SerializedName("id") val id : String,
    @SerializedName("store_name") val store_name : String,
    @SerializedName("bankRek") val bankRek : String,
    @SerializedName("namaRek") val namaRek : String,
    @SerializedName("noRek") val noRek : String,
    @SerializedName("withdrawl") val withdrawl : String,
    @SerializedName("status_withdrawl") val status_withdrawl : String,
    @SerializedName("created_at") val created_at : String
)