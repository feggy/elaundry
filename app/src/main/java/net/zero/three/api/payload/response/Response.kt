package net.zero.three.api.payload.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ResLogin(
    @SerializedName("id") val id : String,
    @SerializedName("name") val name : String,
    @SerializedName("email") val email : String,
    @SerializedName("email_verified_at") val email_verified_at : String,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("updated_at") val updated_at : String,
    @SerializedName("store_name") val store_name : String,
    @SerializedName("no_hp") val no_hp : String,
    @SerializedName("address") val address : String,
    @SerializedName("latitude") val latitude : String,
    @SerializedName("longitude") val longitude : String,
    @SerializedName("profile_photo_path") val profile_photo_path : String,
    @SerializedName("level") val level : String,
    @SerializedName("active") val active : String,
    @SerializedName("profile_photo_url") val profile_photo_url : String
): Serializable

data class ResRegister(
    @SerializedName("name") val name : String,
    @SerializedName("no_hp") val no_hp : String,
    @SerializedName("address") val address : String,
    @SerializedName("email") val email : String,
    @SerializedName("latitude") val latitude : String,
    @SerializedName("longitude") val longitude : String,
    @SerializedName("level") val level : String,
    @SerializedName("password") val password : String
)

data class ResDetailAkun(
    @SerializedName("id") val id : String,
    @SerializedName("name") val name : String,
    @SerializedName("email") val email : String,
    @SerializedName("email_verified_at") val email_verified_at : String,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("updated_at") val updated_at : String,
    @SerializedName("store_name") val store_name : String,
    @SerializedName("no_hp") val no_hp : String,
    @SerializedName("address") val address : String,
    @SerializedName("latitude") val latitude : String,
    @SerializedName("longitude") val longitude : String,
    @SerializedName("profile_photo_path") val profile_photo_path : String,
    @SerializedName("level") val level : String,
    @SerializedName("active") val active : String,
    @SerializedName("profile_photo_url") val profile_photo_url : String
)