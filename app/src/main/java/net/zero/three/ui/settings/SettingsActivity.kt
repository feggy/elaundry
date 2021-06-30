package net.zero.three.ui.settings

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_settings.*
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
//            lytFee.visibility = View.VISIBLE
        }

        biayaPerKg = SessionManager.instance.hargaPerKg
        biayaFee = SessionManager.instance.biayaAdmin
        idMerchant = SessionManager.instance.userId.toString()

        Timber.e("_ $biayaPerKg $biayaFee $idMerchant")

        vPerkg.text = biayaPerKg.toDouble().toCurrency("Rp")
        vFee.text = "$biayaFee%"
    }

    private fun eventUI() {
        vToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        btnLogout.setOnClickListener {
            SessionManager.instance.nohp = ""
            SessionManager.instance.level = ""
            SessionManager.instance.saldo = ""
            SessionManager.instance.hargaPerKg = ""
            SessionManager.instance.userId = ""
            SessionManager.instance.biayaAdmin = ""

            LoadingDialog.show(supportFragmentManager)
            Handler().postDelayed({
                SplashActivity.show(this)
            },1000)
        }

        vPerkg.setOnClickListener {
            InputDialog.show(supportFragmentManager) {
                biayaPerKg = it
                updateBiaya()
            }
        }

        vFee.setOnClickListener {
            InputDialog.show(supportFragmentManager) {
                biayaFee = it
                updateBiaya()
            }
        }
    }

    private fun updateBiaya() {
        LoadingDialog.show(supportFragmentManager)
        _vm.updateBiaya(idMerchant, biayaFee, biayaPerKg).observe(this, {
            LoadingDialog.close(supportFragmentManager)
            when(it?.status) {
                true -> {
                    SessionManager.instance.apply {
                        hargaPerKg = biayaPerKg
                        biayaAdmin = biayaFee
                    }
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