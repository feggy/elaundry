package net.zero.three.ui.order

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.android.synthetic.main.activity_order.vToolbar
import net.zero.three.R
import net.zero.three.api.payload.response.ResStore
import net.zero.three.dialog.AppAlertDialog
import net.zero.three.dialog.ConfirmationDialog
import net.zero.three.dialog.InputDialog
import net.zero.three.dialog.LoadingDialog
import net.zero.three.hideKeyboard
import net.zero.three.toCurrency
import net.zero.three.toEditable
import net.zero.three.ui.MainActivity
import net.zero.three.viewmodel.AuthViewModel
import java.text.SimpleDateFormat
import java.util.*

class OrderActivity : AppCompatActivity() {

    companion object {
        fun show(activity: Activity, resStore: ResStore) {
            val i = Intent(activity, OrderActivity::class.java)
            i.putExtra("resStore", resStore)
            activity.startActivity(i)
        }
    }

    lateinit var _vm: AuthViewModel
    lateinit var resStore: ResStore
    var beratSekarang = 0.0

    var idUser = ""
    var idMerchant = ""
    var namaCucian = ""

    var totalAmount = 0.0
    var adminFee = 0.0
    var grandTotal = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        init()
    }

    private fun init() {
        initData()
        initUI()
        eventUI()
    }

    private fun initData() {
        _vm = ViewModelProvider(this).get(AuthViewModel::class.java)
        resStore = intent.getSerializableExtra("resStore") as ResStore
    }

    private fun initUI() {
        vCatatan.imeOptions = EditorInfo.IME_ACTION_DONE
        vCatatan.setRawInputType(InputType.TYPE_CLASS_TEXT)
        vBerat.text = "0.0".toEditable()
        _vm.berat.observe(this, {
            it?.let {
                beratSekarang = Math.round(it * 100) / 100.0

                if (beratSekarang < 0) {
                    beratSekarang = 0.0
                }
                totalAmount = beratSekarang * 7000
                adminFee = totalAmount * 0.01
                grandTotal = totalAmount + adminFee

                vBerat.text = "$beratSekarang".toEditable()
                vTotalAmount.text = totalAmount.toCurrency("Rp")
                vBiayaLayanan.text = adminFee.toCurrency("Rp")
                vGrandTotal.text = grandTotal.toCurrency("Rp")
            }
        })

        LoadingDialog.show(supportFragmentManager)
        _vm.getDetailAkun().observe(this, {
            LoadingDialog.close(supportFragmentManager)
            when (it?.status) {
                true -> {
                    it.data?.let {
                        idUser = it.id
                        vNama.text = it.name
                        vNohp.text = it.no_hp
                        vAlamat.text = it.address
                        vEmail.text = it.email
                    }
                }
                false -> {

                }
            }
        })

        vNamaLaundry.text = resStore.store_name
        vAlamatLaundry.text = resStore.address

    }

    private fun eventUI() {
        vToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        vBerat.setOnClickListener {
            InputDialog.show(supportFragmentManager) {
                _vm.berat.value = it.toDouble()
            }
        }

        btnShowCustomer.setOnClickListener {
            if (showHideCustomer.visibility == View.VISIBLE) {
                imgShowCustomer.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                showHideCustomer.visibility = View.GONE
            } else {
                imgShowCustomer.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                showHideCustomer.visibility = View.VISIBLE
            }
        }

        btnShowLaundry.setOnClickListener {
            if (showHideLaundry.visibility == View.VISIBLE) {
                imgShowLaundry.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                showHideLaundry.visibility = View.GONE
            } else {
                imgShowLaundry.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                showHideLaundry.visibility = View.VISIBLE
            }
        }

        btnPlus.setOnClickListener {
            _vm.berat.value = beratSekarang + 0.1
        }

        btnMinus.setOnClickListener {
            _vm.berat.value = beratSekarang - 0.1
        }

        btnOrder.setOnClickListener {
            it.hideKeyboard()
            ConfirmationDialog.show(
                supportFragmentManager,
                "Konfirmasi Pembayaran",
                "Mau melakukan pembayaran sekarang?",
                "Bayar",
                "Nanti",
                callbackPositive = {
                    Toast.makeText(
                        applicationContext,
                        "Masuk ke halaman payment method",
                        Toast.LENGTH_LONG
                    ).show()
                },
                callbackNegative = {
                    order()
                },
                close_button = true
            )
        }
    }

    private fun order() {
        idMerchant = resStore.id.toString()
        val currentTime = SimpleDateFormat("ddMMyyyyHHmmSSa").format(Date())
        namaCucian = "Laundry-$currentTime-$idMerchant$idUser"

        LoadingDialog.show(supportFragmentManager)
        _vm.reqOrder(
            idUser,
            idMerchant,
            namaCucian,
            beratSekarang.toString(),
            grandTotal.toString(),
            totalAmount.toString(),
            adminFee.toString(),
            vCatatan.text.toString()
        ).observe(this, {
            LoadingDialog.close(supportFragmentManager)
            when (it?.status) {
                true -> {
                    AppAlertDialog.show(
                        supportFragmentManager,
                        "Sukses",
                        "Berhasil membuat pesanan, mohon tunggu hingga pesanan Anda disetujui oleh pihak laundry",
                        callbackPositive = {
                            MainActivity.show(this)
                        }
                    )
                }
                false -> {
                    AppAlertDialog.show(
                        supportFragmentManager,
                        "Gagal",
                        "Sepertinya terjadi gangguan pada jaringan mengakibat gagal terhubung ke server",
                        error = true
                    )
                }
            }
        })
    }
}