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

    @GET("laundry/detail/{noHp}")
    fun getStoreDetail(@Path("noHp") noHp: String?): Call<Resource<ResDetailAkun>>

    @POST("laundry/order")
    fun reqOrder(@Body reqOrder: ReqOrder): Call<Resource<ResOrder>>

    @POST("laundry/list/store")
    fun getStore(@Body reqStore: ReqStore): Call<Resource<List<ResStore>>>

    @POST("laundry/history/transaksi")
    fun getHistory(@Body reqHistory: ReqHistory): Call<Resource<List<ResHistory>>>

    @POST("laundry/payment")
    fun reqPayment(@Body reqPayment: ReqPayment): Call<Resource<ResPayment>>

    @GET("laundry/detail/order/{orderId}")
    fun getDetailOrder(@Path("orderId") orderId: String?): Call<Resource<ResDetailOrder>>

    @POST("laundry/update/merchant")
    fun updateBiaya(@Body reqBiaya: ReqBiaya): Call<Resource<Any>>

    @POST("laundry/withdrawl")
    fun reqWithdrawal(@Body reqWithdrawal: ReqWithdrawal): Call<Resource<Any>>

    @GET("laundry/list/withdrawl/{idMerchant}")
    fun getWithdrawalHistory(@Path("idMerchant") idMerchant: String?): Call<Resource<List<ResWithdrawalHistory>>>

    @GET("laundry/list/order/merchant/{idMerchant}")
    fun getTransactionCustomer(@Path("idMerchant") idMerchant: String?): Call<Resource<List<ResDetailOrder>>>

    @GET("laundry/status/order/{orderId}/{status}")
    fun updateStatus(
        @Path("orderId") orderId: String?,
        @Path("status") status: String?,
    ): Call<Resource<ResHistory>>

    @GET("laundry/list/jenis/cucian/merchant/{idMerchant}")
    fun getHarga(
        @Path("idMerchant") orderId: String?
    ): Call<Resource<List<ResHarga>>>
}