package net.zero.three.api.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import net.zero.three.api.RetrofitFactory
import net.zero.three.api.payload.Resource
import net.zero.three.api.payload.request.ReqLogin
import net.zero.three.api.payload.request.ReqRegister
import net.zero.three.api.payload.response.ResDetailAkun
import net.zero.three.api.payload.response.ResLogin
import net.zero.three.api.payload.response.ResRegister
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
        lng: String
    ): LiveData<Resource<ResRegister>> {
        val liveData = MutableLiveData<Resource<ResRegister>>()

        auth.register(ReqRegister(nama, nohp, alamat, email, lat, lng, password))
            .enqueue(object : Callback<Resource<ResRegister>> {
                override fun onResponse(
                    call: Call<Resource<ResRegister>>,
                    response: Response<Resource<ResRegister>>
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

                override fun onFailure(call: Call<Resource<ResRegister>>, t: Throwable) {
                    Log.e("ONFAILURE", "$t")
                }

            })
        return liveData
    }

    fun getDetailAkun() : LiveData<Resource<List<ResDetailAkun>>> {
        val liveData = MutableLiveData<Resource<List<ResDetailAkun>>>()

        auth.getAkunDetail(SessionManager.instance.nohp).enqueue(object : Callback<Resource<List<ResDetailAkun>>> {
            override fun onResponse(
                call: Call<Resource<List<ResDetailAkun>>>,
                response: Response<Resource<List<ResDetailAkun>>>
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

            override fun onFailure(call: Call<Resource<List<ResDetailAkun>>>, t: Throwable) {
                Log.e("ONFAILURE", "$t")
            }
        })
        return liveData
    }
}