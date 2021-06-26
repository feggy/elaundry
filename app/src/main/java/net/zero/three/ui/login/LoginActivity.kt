package net.zero.three.ui.login

import android.app.Activity
import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_login.*
import net.zero.three.R
import net.zero.three.api.payload.Resource
import net.zero.three.dialog.AppAlertDialog
import net.zero.three.dialog.LoadingDialog
import net.zero.three.hideKeyboard
import net.zero.three.persistant.SessionManager
import net.zero.three.ui.MainActivity
import net.zero.three.ui.register.RegisterActivity
import net.zero.three.viewmodel.AuthViewModel
import timber.log.Timber

class LoginActivity : AppCompatActivity() {
    companion object {
        fun show(activity: Activity) {
            val i = Intent(activity, LoginActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            activity.startActivity(i)
        }
    }

    private lateinit var _vm: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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

    }

    private fun eventUI() {
        btnLogin.setOnClickListener {
            it.hideKeyboard()
            if (validation()) {
                LoadingDialog.show(supportFragmentManager)
                _vm.login(etUsername.text.toString(), etPassword.text.toString()).observe(this, {
                    LoadingDialog.close(supportFragmentManager)
                    when(it?.status) {
                        true -> {
                            it.data?.let {
                                SessionManager.instance.nohp = it.no_hp
                                MainActivity.show(this)
                            }
                        }
                        false -> {
                            AppAlertDialog.show(
                                supportFragmentManager,
                                "Oops",
                                it.message,
                                error = true
                            )
                        }
                    }
                })
            }
        }

        btnRegister.setOnClickListener {
            RegisterActivity.show(this)
        }
    }

    private fun validation(): Boolean {
        val nohp = etUsername.text.toString()
        val pass = etPassword.text.toString()

        if (nohp.isNullOrEmpty()) {
            AppAlertDialog.show(
                supportFragmentManager,
                "Oops",
                "Nomor hp tidak boleh kosong",
                error = true
            )
            return false
        } else if (pass.isNullOrEmpty()) {
            AppAlertDialog.show(
                supportFragmentManager,
                "Oops",
                "Password tidak boleh kosong",
                error = true
            )
            return false
        }
        return true
    }
}