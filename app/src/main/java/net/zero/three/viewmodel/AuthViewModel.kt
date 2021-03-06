package net.zero.three.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import net.zero.three.api.payload.response.ResHarga
import net.zero.three.api.repository.AuthRepository

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private var authRepository: AuthRepository = AuthRepository()

    var imageStore = MutableLiveData<Bitmap>()

    var berat = MutableLiveData<Double>()

    val hargaPerKg = MutableLiveData<String>()

    fun login(nohp: String, pass: String) = authRepository.login(nohp, pass)

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
    ) = authRepository.register(
        nama,
        nohp,
        password,
        alamat,
        email,
        lat,
        lng,
        level,
        imageStore,
        storeName
    )

    fun getDetailAkun() = authRepository.getDetailAkun()

    fun getStoreDetail(noHp: String) = authRepository.getStoreDetail(noHp)

    fun reqOrder(
        idUser: String,
        idMerchant: String,
        namaCucian: String,
        berat: String,
        amount: String,
        amountSatuan: String,
        fee: String,
        catatan: String,
        idJenisCucian: String
    ) = authRepository.reqOrder(
        idUser,
        idMerchant,
        namaCucian,
        berat,
        amount,
        amountSatuan,
        fee,
        catatan,
        idJenisCucian
    )

    fun getStore(nearest: Boolean? = false, lat: String? = "", long: String? = "") =
        authRepository.getStore(nearest, lat, long)

    fun getHistory(status: String) = authRepository.getHistory(status)

    fun reqPayment(
        payment_method: String,
        amount: String,
        fee: String,
        orderId: String,
        nama: String,
        email: String,
        noHp: String
    ) = authRepository.reqPayment(payment_method, amount, fee, orderId, nama, email, noHp)

    fun getDetailOrder(orderId: String) = authRepository.getDetailOrder(orderId)

    fun updateBiaya(
        idMerchant: String,
        fee: String,
        harga: String,
        idJenisCucian: String
    ) = authRepository.updateBiaya(idMerchant, fee, harga, idJenisCucian)

    fun reqWithdrawal(
        noHp: String,
        withdrawl: String,
        noRek: String,
        bankRek: String,
        namaReka: String
    ) = authRepository.reqWithdrawal(noHp, withdrawl, noRek, bankRek, namaReka)

    fun getWithdrawalHistory(
        idMerchant: String
    ) = authRepository.getWithdrawalHistory(idMerchant)

    fun getTransactionCustomer(
        idMerchant: String
    ) = authRepository.getTransactionCustomer(idMerchant)

    fun updateStatus(
        orderId: String,
        status: String
    ) = authRepository.updateStatus(orderId, status)

    fun getHarga(idMerchant: String) = authRepository.getHarga(idMerchant)
}