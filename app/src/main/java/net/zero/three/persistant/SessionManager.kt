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


    /*
    .
    Contoh penerapan SessionManager
    .
    var example: String?
        get() = pref.getString(SharedPrefrenceConstant.EXAMPLE, "")
        set(value) {
            pref.edit {
                putString(SharedPrefrenceConstant.EXAMPLE, value)
            }
        }
    */

    var nohp: String?
        get() = pref.getString("NOHP", "")
        set(value) {
            pref.edit {
                putString("NOHP", value)
            }
        }


}