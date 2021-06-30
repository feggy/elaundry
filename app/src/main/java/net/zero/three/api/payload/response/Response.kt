package net.zero.three.api.payload.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ResLogin(
    @SerializedName("id") var id: String,
    @SerializedName("name") var name: String,
    @SerializedName("email") var email: String,
    @SerializedName("email_verified_at") var email_verified_at: String,
    @SerializedName("created_at") var created_at: String,
    @SerializedName("updated_at") var updated_at: String,
    @SerializedName("store_name") var store_name: String,
    @SerializedName("no_hp") var no_hp: String,
    @SerializedName("address") var address: String,
    @SerializedName("latitude") var latitude: String,
    @SerializedName("longitude") var longitude: String,
    @SerializedName("profile_photo_path") var profile_photo_path: String,
    @SerializedName("level") var level: String,
    @SerializedName("active") var active: String,
    @SerializedName("profile_photo_url") var profile_photo_url: String
) : Serializable

data class ResRegister(
    @SerializedName("name") var name: String,
    @SerializedName("no_hp") var no_hp: String,
    @SerializedName("address") var address: String,
    @SerializedName("email") var email: String,
    @SerializedName("latitude") var latitude: String,
    @SerializedName("longitude") var longitude: String,
    @SerializedName("level") var level: String,
    @SerializedName("password") var password: String,
    @SerializedName("store_name") var store_name: String,
    @SerializedName("id") var id: Int,
    @SerializedName("image_store") var image_store: String,
    @SerializedName("profile_photo_url") var profile_photo_url: String
)

data class ResDetailAkun(
    @SerializedName("id") var id: String,
    @SerializedName("name") var name: String,
    @SerializedName("email") var email: String,
    @SerializedName("email_verified_at") var email_verified_at: String,
    @SerializedName("created_at") var created_at: String,
    @SerializedName("updated_at") var updated_at: String,
    @SerializedName("store_name") var store_name: String,
    @SerializedName("no_hp") var no_hp: String,
    @SerializedName("address") var address: String,
    @SerializedName("latitude") var latitude: String,
    @SerializedName("longitude") var longitude: String,
    @SerializedName("profile_photo_path") var profile_photo_path: String,
    @SerializedName("level") var level: String,
    @SerializedName("active") var active: String,
    @SerializedName("profile_photo_url") var profile_photo_url: String,
    @SerializedName("image_store") var image_store: String,
    @SerializedName("saldo") var saldo: String,
    @SerializedName("fee") var fee: String,
    @SerializedName("laundry_per_kg") var laundry_per_kg: String
)

data class ResOrder(
    @SerializedName("id_merchant") var  id_merchant: String,
    @SerializedName("id_user") var id_user: String,
    @SerializedName("nama_cucian") var  nama_cucian: String,
    @SerializedName("berat") var  berat: String,
    @SerializedName("amount_satuan") var  amount_satuan: String,
    @SerializedName("id") var  id: String,
    @SerializedName("created_at") var  created_at: String,
    @SerializedName("updated_at") var  updated_at: String
)

data class ResStore(
    @SerializedName("id") var id : Int,
    @SerializedName("store_name") var store_name : String?,
    @SerializedName("address") var address : String?,
    @SerializedName("latitude") var latitude : String,
    @SerializedName("longitude") var longitude : String,
    @SerializedName("image_store") var image_store : String,
    @SerializedName("distance") var distance : String,
    @SerializedName("profile_photo_url") var profile_photo_url : String,
    @SerializedName("no_hp") var no_hp : String
): Serializable

data class ResHistory(
    @SerializedName("id") var id : String,
    @SerializedName("id_user") var id_user : String,
    @SerializedName("id_merchant") var id_merchant : String,
    @SerializedName("nama_cucian") var nama_cucian : String,
    @SerializedName("berat") var berat : String,
    @SerializedName("amount_satuan") var amount_satuan : String,
    @SerializedName("amount") var amount : String,
    @SerializedName("status") var status : String,
    @SerializedName("status_pembayaran") var status_pembayaran : String,
    @SerializedName("status_pengerjaan") var status_pengerjaan : String,
    @SerializedName("created_at") var created_at : String,
    @SerializedName("updated_at") var updated_at : String,
    @SerializedName("fee") var fee : String,
    @SerializedName("catatan") var catatan : String,
    @SerializedName("store_name") var store_name : String
)

data class Antrian(
    var orderId: String,
    var id_merchant: String,
    var nama: String,
    var no_hp: String,
    var status_pengerjaan: String,
    var created_at: String
): Serializable

data class ResPayment(
    @SerializedName("id_order") var id_order : String,
    @SerializedName("id_invoice") var id_invoice : String,
    @SerializedName("amount") var amount : String,
    @SerializedName("payment_method") var payment_method : String,
    @SerializedName("status") var status : String,
    @SerializedName("expired_time") var expired_time : String,
    @SerializedName("created_at") var created_at : String,
    @SerializedName("updated_at") var updated_at : String,
    @SerializedName("reference") var reference : String,
    @SerializedName("pay_code") var pay_code : String,
    @SerializedName("id") var id : String,
    @SerializedName("fee") var fee : String,
    @SerializedName("total_bayar") var total_bayar : String
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
    @SerializedName("total_bayar") var total_bayar : String,
    @SerializedName("status_payment") var status_payment : String
)

data class ResWithdrawalHistory(
    @SerializedName("id") var id : String,
    @SerializedName("store_name") var store_name : String,
    @SerializedName("bankRek") var bankRek : String,
    @SerializedName("namaRek") var namaRek : String,
    @SerializedName("noRek") var noRek : String,
    @SerializedName("withdrawl") var withdrawl : String,
    @SerializedName("status_withdrawl") var status_withdrawl : String,
    @SerializedName("created_at") var created_at : String
)