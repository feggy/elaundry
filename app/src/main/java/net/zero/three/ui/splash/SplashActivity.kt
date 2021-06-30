package net.zero.three.ui.splash

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import net.zero.three.R
import net.zero.three.persistant.SessionManager
import net.zero.three.ui.MainActivity
import net.zero.three.ui.login.LoginActivity
import net.zero.three.viewmodel.AuthViewModel
import timber.log.Timber

class SplashActivity : AppCompatActivity() {
    companion object {
        fun show(activity: Activity) {
            val i = Intent(activity, SplashActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            activity.startActivity(i)
            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    private val reqPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (checkPermissions()) {
                Handler().postDelayed({
                    LoginActivity.show(this)
                }, 1000)
            } else {
                requestPermissions()
            }
        }

    private val perms = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private lateinit var _vm: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        _vm = ViewModelProvider(this).get(AuthViewModel::class.java)

        if (checkPermissions()) {
            Handler().postDelayed({
                if (SessionManager.instance.nohp.isNullOrEmpty()) {
                    LoginActivity.show(this)
                } else {
                    getDetailAkun()
                }
            }, 1000)
        } else {
            requestPermissions()
        }
    }

    private fun getDetailAkun() {
        _vm.getDetailAkun().observe(this, {
            when (it?.status) {
                true -> {
                    it.data?.let {
                        SessionManager.instance.hargaPerKg = it.laundry_per_kg
                        SessionManager.instance.biayaAdmin = it.fee
                        SessionManager.instance.userId = it.id
                        SessionManager.instance.level = it.level
                        SessionManager.instance.saldo = it.saldo
                        MainActivity.show(this)
                    }
                }
            }
        })
    }

    private fun checkPermissions(): Boolean {
        perms.forEach {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    it
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    private fun requestPermissions() {
        reqPermission.launch(perms)
    }
}