package net.zero.three.ui.profile

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_profile.*
import net.zero.three.R
import net.zero.three.dialog.LoadingDialog
import net.zero.three.persistant.SessionManager
import net.zero.three.ui.splash.SplashActivity
import net.zero.three.viewmodel.AuthViewModel

class ProfileActivity : AppCompatActivity() {

    companion object {
        fun show(activity: Activity) {
            val i = Intent(activity, ProfileActivity::class.java)
            activity.startActivity(i)
        }
    }

    private lateinit var _vm: AuthViewModel
    var lat = ""
    var long = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

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
                    it.data?.let {
                        vNama.text = it.name
                        vEmail.text = it.email
                        vAddress.text = it.address
                        vNohp.text = it.no_hp
                        vLevel.text = it.level
                        
                        long = it.longitude
                        lat = it.latitude

                        Glide.with(applicationContext)
                            .load(it.profile_photo_url)
                            .placeholder(R.drawable.avatar)
                            .error(R.drawable.avatar)
                            .into(vAvatar)

                        val url = "https://elaundry.masuk.id"

                        Glide.with(applicationContext)
                            .load(url+it.image_store)
                            .error(R.drawable.ic_close)
                            .listener(object : RequestListener<Drawable> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    return false
                                }

                                override fun onResourceReady(
                                    resource: Drawable?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    dataSource: DataSource?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    imgToko.scaleType = ImageView.ScaleType.CENTER_CROP
                                    return false
                                }

                            })
                            .into(imgToko)

                        getImageAddress()
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

        btnRefresh.setOnClickListener {
            getImageAddress()
        }
    }

    private fun getImageAddress() {
        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(applicationContext)
            .load(
                "https://maps.geoapify.com/v1/staticmap?style=osm-carto&width=700&height=300&" +
                        "center=lonlat:$long,$lat&" +
                        "zoom=17.7327&marker=lonlat:$long,$lat;" +
                        "type:material;color:%23ff3421;icontype:awesome&apiKey=a9b2423c37d747ddaef9fd169663b80a"
            )
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    btnRefresh.visibility = View.VISIBLE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    btnRefresh.visibility = View.GONE
                    return false
                }

            })
            .placeholder(circularProgressDrawable)
            .error(R.drawable.refresh)
            .into(imgAddress)
    }
}