package net.zero.three.api.private_interface


import net.zero.three.api.payload.Resource
import net.zero.three.api.payload.request.*
import net.zero.three.api.payload.response.*
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
    fun getAkunDetail(@Path("noHp") noHp: String?): Call<Resource<ResDetailAkun>>

    @POST("laundry/order")
    fun reqOrder(@Body reqOrder: ReqOrder): Call<Resource<ResOrder>>

    @POST("laundry/list/store")
    fun getStore(@Body reqStore: ReqStore): Call<Resource<List<ResStore>>>

    @POST("laundry/history/transaksi")
    fun getHistory(@Body reqHistory: ReqHistory): Call<Resource<List<ResHistory>>>

    @POST("laundry/payment")
    fun reqPayment(@Body reqPayment: ReqPayment) : Call<Resource<ResPayment>>

    @GET("laundry/detail/order/{orderId}")
    fun getDetailOrder(@Path("orderId") orderId: String?): Call<Resource<ResDetailOrder>>
}