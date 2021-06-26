package net.zero.three.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import net.zero.three.api.repository.AuthRepository

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private var authRepository: AuthRepository = AuthRepository()

    var imageStore = MutableLiveData<Bitmap>()

    var berat = MutableLiveData<Double>()

    fun login(nohp: String, pass: String) = authRepository.login(nohp, pass)

    fun register(
        nama: String,
        nohp: String,
        password: String,
        alamat: String,
        email: String,
        lat: String,
        lng: String
    ) = authRepository.register(nama, nohp, password, alamat, email, lat, lng)

    fun getDetailAkun() = authRepository.getDetailAkun()
}