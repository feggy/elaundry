package net.zero.three.persistant

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import net.zero.three.App
import net.zero.three.constants.SharedPrefrenceConstant.Companion.SHARED_PREF_NAME

class SessionManager(val context: Context) {

    companion object {
        val instance: SessionManager by lazy {
            SessionManager(App.context)
        }
    }

    private var pref: SharedPreferences

    init {
        pref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    var nohp: String?
        get() = pref.getString("NOHP", "")
        set(value) {
            pref.edit {
                putString("NOHP", value)
            }
        }

    var hargaPerKg: String
        get() = pref.getString("hargaPerKg", "") ?: "0"
        set(value) {
            pref.edit {
                putString("hargaPerKg", value)
            }
        }

    var biayaAdmin: String
        get() = pref.getString("biayaAdmin", "") ?: "0"
        set(value) {
            pref.edit {
                putString("biayaAdmin", value)
            }
        }

    var userId: String?
        get() = pref.getString("userId", "")
        set(value) {
            pref.edit {
                putString("userId", value)
            }
        }

    var level: String?
        get() = pref.getString("level", "")
        set(value) {
            pref.edit {
                putString("level", value)
            }
        }

    var saldo: String
        get() = pref.getString("saldo", "") ?: "0"
        set(value) {
            pref.edit {
                putString("saldo", value)
            }
        }

}