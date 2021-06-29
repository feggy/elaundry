package net.zero.three.ui.withdrawal

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_withdrawal.*
import net.zero.three.R
import net.zero.three.dialog.AppAlertDialog
import net.zero.three.dialog.ListUniversalDialog
import net.zero.three.dialog.ListUniversalDialog.Item
import net.zero.three.dialog.LoadingDialog
import net.zero.three.persistant.SessionManager
import net.zero.three.toCurrency
import net.zero.three.toEditable
import net.zero.three.ui.MainActivity
import net.zero.three.viewmodel.AuthViewModel
import timber.log.Timber
import java.lang.NumberFormatException
import java.lang.Exception
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


class WithdrawalActivity : AppCompatActivity() {

    companion object {
        fun show(activity: Activity) {
            val i = Intent(activity, WithdrawalActivity::class.java)
            activity.startActivity(i)
        }
    }

    lateinit var _vm: AuthViewModel
    var saldo = ""
    var bank = ""
    var nama = ""
    var norek = ""
    var jumlah = ""
    var nohp = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_withdrawal)

        init()
    }

    override fun onStart() {
        super.onStart()
        window.statusBarColor = resources.getColor(R.color.status_bar_color)
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
        saldo = SessionManager.instance.saldo
        vCheck.text = "Tarik dana saat ini (${saldo.toDouble().toCurrency("Rp")})"
        vCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                vJumlah.text = saldo.toEditable()
            } else {
                vJumlah.text = "".toEditable()
            }
        }

        vJumlah.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (!s.toString()
                        .matches("^\\$(\\d{1,3}(,\\d{3})*|(\\d+))(\\.\\d{2})?$".toRegex())
                ) {
                    var userInput = "" + s.toString().replace("[^\\d]".toRegex(), "")
                    var total: Long?

                    vJumlah.removeTextChangedListener(this)
                    if (userInput.isNotEmpty()) {
                        total = java.lang.Long.parseLong(userInput)
                        val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
                        formatter.applyPattern("#,###,###,###")
                        val formattedString = formatter.format(total)
                        vJumlah.setTextKeepState(formattedString.replace(",", "."))
                    } else {
                        vJumlah.setTextKeepState("")
                    }

                    Selection.setSelection(vJumlah.text, vJumlah.text.toString().length)
                    vJumlah.addTextChangedListener(this)
                }
            }
        })

        btnBank.setOnClickListener {
            ListUniversalDialog.show(supportFragmentManager, listBank) {
                bank = it.name
                btnBank.text = bank
            }
        }
    }

    private fun eventUI() {
        btnNext.setOnClickListener {
            jumlah = vJumlah.text.toString().replace(".", "")
            norek = vNoRek.text.toString()
            nama = vNamaRek.text.toString()
            nohp = SessionManager.instance.nohp!!

            if (validation()) {
                LoadingDialog.show(supportFragmentManager)
                _vm.reqWithdrawal(
                    nohp,
                    jumlah,
                    norek,
                    bank,
                    nama
                ).observe(this, {
                    LoadingDialog.close(supportFragmentManager)
                    when (it?.status) {
                        true -> {
                            AppAlertDialog.show(
                                supportFragmentManager,
                                "SUKSES",
                                "Permintaan penarikan dana sudah diterima dan akan segera di proses oleh Admin",
                                callbackPositive = {
                                    MainActivity.show(this)
                                }
                            )
                        }
                        false -> {
                            AppAlertDialog.show(
                                supportFragmentManager,
                                "Oops",
                                "Terjadi suatu kesalahan",
                                error = true
                            )
                        }
                    }
                })
            }
        }
    }

    private fun validation(): Boolean {
        if (vNoRek.text.isEmpty()) {
            AppAlertDialog.show(
                supportFragmentManager,
                "Oops",
                "Nomor rekening tidak boleh kosong",
                error = true
            )
            return false
        } else if (vNamaRek.text.isEmpty()) {
            AppAlertDialog.show(
                supportFragmentManager,
                "Oops",
                "Nama rekening tidak boleh kosong",
                error = true
            )
            return false
        } else if (vJumlah.text.isEmpty()) {
            AppAlertDialog.show(
                supportFragmentManager,
                "Oops",
                "Jumlah dana yang ditarik tidak boleh kosong",
                error = true
            )
            return false
        } else if (btnBank.text.isEmpty()) {
            AppAlertDialog.show(
                supportFragmentManager,
                "Oops",
                "Bank tidak boleh kosong",
                error = true
            )
            return false
        } else if (jumlah.toInt() > saldo.toInt()) {
            AppAlertDialog.show(
                supportFragmentManager,
                "Oops",
                "Saldo tidak cukup",
                error = true
            )
            return false
        } else if (jumlah.toInt() < 10000) {
            AppAlertDialog.show(
                supportFragmentManager,
                "Oops",
                "Minimal penarikan Rp10.000",
                error = true
            )
            return false
        }

        return true
    }


    val listBank = arrayListOf(
        Item(5, "BANK BCA"),
        Item(1, "BANK BRI"),
        Item(3, "BANK BNI"),
        Item(2, "BANK MANDIRI"),
        Item(4, "BANK SYARIAH INDONESIA"),
        Item(5, "PERMATA BANK")
    )
}