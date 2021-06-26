package net.zero.three.ui.order

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_order.*
import net.zero.three.R
import net.zero.three.dialog.ConfirmationDialog
import net.zero.three.toEditable
import net.zero.three.viewmodel.AuthViewModel

class OrderActivity : AppCompatActivity() {

    companion object {
        fun show(activity: Activity) {
            val i = Intent(activity, OrderActivity::class.java)
            activity.startActivity(i)
        }
    }

    lateinit var _vm: AuthViewModel
    var beratSekarang = 0.0

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
    }

    private fun initUI() {
        vBerat.text = "0.0 kg".toEditable()
        _vm.berat.observe(this, {
            it?.let {
                beratSekarang = Math.round(it * 100) / 100.0

                if (beratSekarang < 0) {
                    beratSekarang = 0.0
                }
                vBerat.text = "$beratSekarang".toEditable()
            }
        })
    }

    private fun eventUI() {
        vToolbar.setNavigationOnClickListener {
            onBackPressed()
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
                    Toast.makeText(
                        applicationContext,
                        "Tampil detail order pesanan di proses menunggu persetujuan merchant",
                        Toast.LENGTH_LONG
                    ).show()
                },
                close_button = true
            )
        }

    }
}