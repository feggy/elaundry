package net.zero.three.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import net.zero.three.R
import net.zero.three.dialog.LoadingDialog
import net.zero.three.ui.profile.ProfileActivity
import net.zero.three.viewmodel.AuthViewModel

class MainActivity : AppCompatActivity() {

    companion object {
        fun show (activity: Activity) {
            val i = Intent(activity, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            activity.startActivity(i)
        }
    }

    lateinit var _vm: AuthViewModel

    var mylat = ""
    var mylong = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        LoadingDialog.show(supportFragmentManager)
        _vm.getDetailAkun().observe(this, {
            LoadingDialog.close(supportFragmentManager)
            when (it?.status) {
                true -> {
                    it.data?.get(0)?.let {
                        Glide.with(applicationContext)
                            .load(it.profile_photo_url)
                            .placeholder(R.drawable.avatar)
                            .error(R.drawable.avatar)
                            .into(vAvatar)

                        vNama.text = it.name
                        vLevel.text = it.level

                        mylat = it.latitude
                        mylong = it.longitude
                    }
                }
                false -> {

                }
            }
        })

    }

    private fun eventUI() {
        vAvatar.setOnClickListener {
            ProfileActivity.show(this)
        }
    }

    fun distance(
        lat1: Double, lat2: Double, lon1: Double,
        lon2: Double, el1: Double, el2: Double
    ): Double {
        val R = 6371 // Radius of the earth
        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)
        val a = (Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + (Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2)))
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        var distance = R * c * 1000 // convert to meters
        val height = el1 - el2
        distance = Math.pow(distance, 2.0) + Math.pow(height, 2.0)
        return Math.sqrt(distance)
    }
}