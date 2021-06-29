package net.zero.three.api.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import net.zero.three.api.RetrofitFactory
import net.zero.three.api.payload.Resource
import net.zero.three.api.payload.request.*
import net.zero.three.api.payload.response.*
import net.zero.three.persistant.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class AuthRepository() {

    private val auth = RetrofitFactory.Service.auth

    fun login(nohp: String, pass: String): LiveData<Resource<ResLogin>> {
        val liveData = MutableLiveData<Resource<ResLogin>>()

        auth.login(ReqLogin(nohp, pass)).enqueue(object : Callback<Resource<ResLogin>> {
            override fun onResponse(
                call: Call<Resource<ResLogin>>,
                response: Response<Resource<ResLogin>>
            ) {
                Timber.e("RESPONSE ${response.body()}")
                Log.e("RESPONSE", "_ ${response.body()}")
                if (response.isSuccessful) {
                    liveData.value = response.body()
                } else {
                    Log.e("UNSUCCESSFUL", "xxxx")
                    liveData.value = Resource(
                        false,
                        null,
                        response.body()?.message.toString(),
                        response.body()?.code
                    )
                }
            }

            override fun onFailure(call: Call<Resource<ResLogin>>, t: Throwable) {
                Log.e("ONFAILURE", "$t")
            }

        })
        return liveData
    }

    fun register(
        nama: String,
        nohp: String,
        password: String,
        alamat: String,
        email: String,
        lat: String,
        lng: String,
        level: String,
        imageStore: String,
        storeName: String
    ): LiveData<Resource<ResRegister>> {
        val liveData = MutableLiveData<Resource<ResRegister>>()

        auth.register(
            ReqRegister(
                nama,
                nohp,
                alamat,
                email,
                lat,
                lng,
                password,
                level,
                imageStore,
                storeName
            )
        )
            .enqueue(object : Callback<Resource<ResRegister>> {
                override fun onResponse(
                    call: Call<Resource<ResRegister>>,
                    response: Response<Resource<ResRegister>>
                ) {
                    Timber.e("_RESPONSE ${response.body()}")
                    if (response.isSuccessful) {
                        liveData.value = response.body()
                    } else {
                        liveData.value = Resource(
                            false,
                            null,
                            response.body()?.message.toString(),
                            response.body()?.code
                        )
                    }
                }

                override fun onFailure(call: Call<Resource<ResRegister>>, t: Throwable) {
                    Log.e("ONFAILURE", "$t")
                }

            })
        return liveData
    }

    fun getDetailAkun(): LiveData<Resource<ResDetailAkun>> {
        val liveData = MutableLiveData<Resource<ResDetailAkun>>()

        auth.getAkunDetail(SessionManager.instance.nohp)
            .enqueue(object : Callback<Resource<ResDetailAkun>> {
                override fun onResponse(
                    call: Call<Resource<ResDetailAkun>>,
                    response: Response<Resource<ResDetailAkun>>
                ) {
                    if (response.isSuccessful) {
                        liveData.value = response.body()
                    } else {
                        liveData.value = Resource(
                            false,
                            null,
                            response.body()?.message.toString(),
                            response.body()?.code
                        )
                    }
                }

                override fun onFailure(call: Call<Resource<ResDetailAkun>>, t: Throwable) {
                    Log.e("ONFAILURE", "$t")
                }
            })
        return liveData
    }

    fun reqOrder(
        idUser: String,
        idMerchant: String,
        namaCucian: String,
        berat: String,
        amount: String,
        amountSatuan: String,
        fee: String,
        catatan: String
    ): LiveData<Resource<ResOrder>> {
        val liveData = MutableLiveData<Resource<ResOrder>>()

        auth.reqOrder(
            ReqOrder(
                idUser,
                idMerchant,
                namaCucian,
                berat,
                amount,
                amountSatuan,
                fee,
                catatan
            )
        )
            .enqueue(object : Callback<Resource<ResOrder>> {
                override fun onResponse(
                    call: Call<Resource<ResOrder>>,
                    response: Response<Resource<ResOrder>>
                ) {
                    if (response.isSuccessful) {
                        liveData.value = response.body()
                    } else {
                        liveData.value = Resource(
                            false,
                            null,
                            response.body()?.message.toString(),
                            response.body()?.code
                        )
                    }
                }

                override fun onFailure(call: Call<Resource<ResOrder>>, t: Throwable) {
                    Log.e("ONFAILURE", "$t")
                }
            })
        return liveData
    }

    fun getStore(
        nearest: Boolean? = false,
        lat: String? = "",
        long: String? = ""
    ): LiveData<Resource<List<ResStore>>> {
        val liveData = MutableLiveData<Resource<List<ResStore>>>()

        auth.getStore(ReqStore(nearest, lat, long))
            .enqueue(object : Callback<Resource<List<ResStore>>> {
                override fun onResponse(
                    call: Call<Resource<List<ResStore>>>,
                    response: Response<Resource<List<ResStore>>>
                ) {
                    if (response.isSuccessful) {
                        liveData.value = response.body()
                    } else {
                        liveData.value = Resource(
                            false,
                            null,
                            response.body()?.message.toString(),
                            response.body()?.code
                        )
                    }
                }

                override fun onFailure(call: Call<Resource<List<ResStore>>>, t: Throwable) {
                    Log.e("ONFAILURE", "$t")
                }
            })
        return liveData
    }

    fun getHistory(status: String): LiveData<Resource<List<ResHistory>>> {
        val liveData = MutableLiveData<Resource<List<ResHistory>>>()

        auth.getHistory(ReqHistory(SessionManager.instance.nohp, status))
            .enqueue(object : Callback<Resource<List<ResHistory>>> {
                override fun onResponse(
                    call: Call<Resource<List<ResHistory>>>,
                    response: Response<Resource<List<ResHistory>>>
                ) {
                    if (response.isSuccessful) {
                        liveData.value = response.body()
                    } else {
                        liveData.value = Resource(
                            false,
                            null,
                            response.body()?.message.toString(),
                            response.body()?.code
                        )
                    }
                }

                override fun onFailure(call: Call<Resource<List<ResHistory>>>, t: Throwable) {
                    Log.e("ONFAILURE", "$t")
                }
            })
        return liveData
    }

    fun reqPayment(
        payment_method: String,
        amount: String,
        fee: String,
        orderId: String,
        nama: String,
        email: String,
        noHp: String
    ): LiveData<Resource<ResPayment>> {
        val liveData = MutableLiveData<Resource<ResPayment>>()

        auth.reqPayment(ReqPayment(payment_method, amount, fee, orderId, nama, email, noHp))
            .enqueue(object : Callback<Resource<ResPayment>> {
                override fun onResponse(
                    call: Call<Resource<ResPayment>>,
                    response: Response<Resource<ResPayment>>
                ) {
                    if (response.isSuccessful) {
                        liveData.value = response.body()
                    } else {
                        liveData.value = Resource(
                            false,
                            null,
                            response.body()?.message.toString(),
                            response.body()?.code
                        )
                    }
                }

                override fun onFailure(call: Call<Resource<ResPayment>>, t: Throwable) {
                    Log.e("ONFAILURE", "$t")
                }
            })
        return liveData
    }

    fun getDetailOrder(
        orderId: String
    ): LiveData<Resource<ResDetailOrder>> {
        val liveData = MutableLiveData<Resource<ResDetailOrder>>()

        auth.getDetailOrder(orderId)
            .enqueue(object : Callback<Resource<ResDetailOrder>> {
                override fun onResponse(
                    call: Call<Resource<ResDetailOrder>>,
                    response: Response<Resource<ResDetailOrder>>
                ) {
                    if (response.isSuccessful) {
                        liveData.value = response.body()
                    } else {
                        liveData.value = Resource(
                            false,
                            null,
                            response.body()?.message.toString(),
                            response.body()?.code
                        )
                    }
                }

                override fun onFailure(call: Call<Resource<ResDetailOrder>>, t: Throwable) {
                    Log.e("ONFAILURE", "$t")
                }
            })
        return liveData
    }

    fun updateBiaya(
        idMerchant: String,
        fee: String,
        harga: String
    ): LiveData<Resource<Any>> {
        val liveData = MutableLiveData<Resource<Any>>()

        auth.updateBiaya(ReqBiaya(idMerchant, fee, harga))
            .enqueue(object : Callback<Resource<Any>> {
                override fun onResponse(
                    call: Call<Resource<Any>>,
                    response: Response<Resource<Any>>
                ) {
                    if (response.isSuccessful) {
                        liveData.value = response.body()
                    } else {
                        liveData.value = Resource(
                            false,
                            null,
                            response.body()?.message.toString(),
                            response.body()?.code
                        )
                    }
                }

                override fun onFailure(call: Call<Resource<Any>>, t: Throwable) {
                    Log.e("ONFAILURE", "$t")
                }
            })
        return liveData
    }
}