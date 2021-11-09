package net.zero.three.ui.settings

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_settings.*
import net.zero.three.JENIS
import net.zero.three.R
import net.zero.three.dialog.AppAlertDialog
import net.zero.three.dialog.InputDialog
import net.zero.three.dialog.LoadingDialog
import net.zero.three.persistant.SessionManager
import net.zero.three.toCurrency
import net.zero.three.toEditable
import net.zero.three.ui.splash.SplashActivity
import net.zero.three.viewmodel.AuthViewModel
import timber.log.Timber

class SettingsActivity : AppCompatActivity() {

    companion object {
        fun show(activity: Activity) {
            val i = Intent(activity, SettingsActivity::class.java)
            activity.startActivity(i)
        }
    }

    lateinit var _vm: AuthViewModel

    var biayaPerKg = ""
    var biayaFee = ""
    var idMerchant = ""
    var idReguler = ""
    var idExpress = ""
    var idLipat = ""
    var idSetrika = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

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
        if (SessionManager.instance.level == "Merchant") {
            lytPerkg.visibility = View.VISIBLE
            lytFee.visibility = View.VISIBLE
        }

        biayaFee = SessionManager.instance.biayaAdmin
        idMerchant = SessionManager.instance.userId.toString()

        Timber.e("_ $biayaFee $idMerchant")

        vFee.text = "$biayaFee%"

        _vm.getHarga(idMerchant).observe(this, {
            when (it?.status) {
                true -> {
                    it.data?.let {
                        it.forEach {
                            when (it.jenisCucian) {
                                "Cuci Penuh" -> {
                                    vReguler.text = it.harga.toDouble().toCurrency("Rp")
                                }
                                "Cuci Lipat" -> {
                                    vLipat.text = it.harga.toDouble().toCurrency("Rp")
                                }
                                "Setrika" -> {
                                    vSetrika.text = it.harga.toDouble().toCurrency("Rp")
                                }
                                "Express" -> {
                                    vExpress.text = it.harga.toDouble().toCurrency("Rp")
                                }
                            }
                        }
                    }
                }
                false -> {

                }
            }
        })
    }

    private fun eventUI() {
        vToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        btnLogout.setOnClickListener {
            SessionManager.instance.nohp = ""
            SessionManager.instance.level = ""
            SessionManager.instance.saldo = ""
            SessionManager.instance.userId = ""
            SessionManager.instance.biayaAdmin = ""

            LoadingDialog.show(supportFragmentManager)
            Handler().postDelayed({
                SplashActivity.show(this)
            },1000)
        }

        vReguler.setOnClickListener {
            InputDialog.show(supportFragmentManager) {
                vReguler.text = it.toDouble().toCurrency("Rp")
                idReguler = JENIS.REGULER.id.toString()
                biayaPerKg = it
                updateBiaya(idReguler)
            }
        }

        vExpress.setOnClickListener {
            InputDialog.show(supportFragmentManager) {
                vExpress.text = it.toDouble().toCurrency("Rp")
                idExpress = JENIS.EXPRESS.id.toString()
                biayaPerKg = it
                updateBiaya(idExpress)
            }
        }

        vLipat.setOnClickListener {
            InputDialog.show(supportFragmentManager) {
                vLipat.text = it.toDouble().toCurrency("Rp")
                idLipat = JENIS.LIPAT.id.toString()
                biayaPerKg = it
                updateBiaya(idLipat)
            }
        }

        vSetrika.setOnClickListener {
            InputDialog.show(supportFragmentManager) {
                vSetrika.text = it.toDouble().toCurrency("Rp")
                idSetrika = JENIS.SETRIKA.id.toString()
                biayaPerKg = it
                updateBiaya(idSetrika)
            }
        }
    }

    private fun updateBiaya(idJenis: String) {
        LoadingDialog.show(supportFragmentManager)
        _vm.updateBiaya(idMerchant, biayaFee, biayaPerKg, idJenis).observe(this, {
            LoadingDialog.close(supportFragmentManager)
            when(it?.status) {
                true -> {
                    initUI()
                }
                false -> {
                    AppAlertDialog.show(
                        supportFragmentManager,
                        "Oops",
                        "Terjadi kesalahan",
                        error = true
                    )
                }
            }
        })
    }
}