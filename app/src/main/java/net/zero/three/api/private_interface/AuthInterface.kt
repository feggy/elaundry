package net.zero.three.api.private_interface


import net.zero.three.api.payload.Resource
import net.zero.three.api.payload.request.ReqLogin
import net.zero.three.api.payload.request.ReqRegister
import net.zero.three.api.payload.response.ResDetailAkun
import net.zero.three.api.payload.response.ResLogin
import net.zero.three.api.payload.response.ResRegister
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthInterface {

    @POST("laundry/login")
    fun login(@Body reqLogin: ReqLogin): Call<Resource<ResLogin>>

    @POST("laundry/register")
    fun register(@Body data: ReqRegister): Call<Resource<ResRegister>>

    @GET("laundry/detail/{noHp}")
    fun getAkunDetail(@Path("noHp") noHp: String?): Call<Resource<List<ResDetailAkun>>>
}