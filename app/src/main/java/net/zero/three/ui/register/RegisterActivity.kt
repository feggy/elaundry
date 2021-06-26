package net.zero.three.ui.register

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_register.*
import net.zero.three.R
import net.zero.three.dialog.AppAlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import net.zero.three.dialog.ConfirmationDialog
import net.zero.three.dialog.LoadingDialog
import net.zero.three.ui.login.LoginActivity
import net.zero.three.viewmodel.AuthViewModel
import java.lang.Exception


class RegisterActivity : AppCompatActivity(), LocationListener {

    companion object {
        fun show(activity: Activity) {
            val i = Intent(activity, RegisterActivity::class.java)
            activity.startActivity(i)
        }
    }

    private val perms = Manifest.permission.ACCESS_FINE_LOCATION

    private val reqPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissions ->
            if (permissions) {
                if (checkLocaitonEnbale()) {
                    getLatLong()
                }
            } else {
                AppAlertDialog.show(supportFragmentManager,
                    "Oops",
                    "Aplikasi butuh izin akses lokasi",
                    error = true,
                    callbackPositive = {
                        requestPermissions()
                    })
            }
        }

    private val reqGps =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (checkLocaitonEnbale()) {
                getLatLong()
            }
        }

    var lat: Double = 0.0
    var lng: Double = 0.0
    var loc: Location? = null
    var MIN_DISTANCE_CHANGE_FOR_UPDATES = 10
    var MIN_TIME_BW_UPDATES = 1000 * 60 * 1
    var locationManager: LocationManager? = null

    // flag for GPS status
    var isGPSEnabled = false

    // flag for network status
    var isNetworkEnabled = false

    // flag for GPS status
    var canGetLocation = false

    var enableRegister = false

    var nama = ""
    var nohp = ""
    var pass = ""
    var email = ""
    var alamat = ""
    var message = ""

    lateinit var _vm: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

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
        vToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        btnRegister.setOnClickListener {
            if (checkPermission()) {
                if (checkLocaitonEnbale()) {
                    if (!enableRegister) {
                        getLatLong()
                    } else {
                        ConfirmationDialog.show(
                            supportFragmentManager,
                            "Konfirmasi Koordinat",
                            "Apakah titik koordinat lokasi rumah anda sudah tepat?",
                            title_positive = "Sudah",
                            title_negative = "Belum",
                            callbackPositive = {
                                register()
                            },
                            callbackNegative = {
                                getLatLong()
                            }
                        )
                    }
                }
            } else {
                requestPermissions()
            }
        }
    }

    private fun register() {
        nama = etNama.text.toString()
        nohp = etUsername.text.toString()
        alamat = etAlamat.text.toString()
        pass = etPassword.text.toString()
        email = etEmail.text.toString()

        LoadingDialog.show(supportFragmentManager)

        if (validation()) {
            _vm.register(nama, nohp, pass, alamat, email, lat.toString(), lng.toString())
                .observe(this, {
                    LoadingDialog.close(supportFragmentManager)
                    when (it?.status) {
                        true -> {
                            AppAlertDialog.show(
                                supportFragmentManager,
                                "SUKSES",
                                "Berhasil mendaftar baru",
                                callbackPositive = {
                                    LoginActivity.show(this)
                                }
                            )
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
        } else {
            LoadingDialog.close(supportFragmentManager)
            AppAlertDialog.show(
                supportFragmentManager,
                "Oops",
                message,
                error = true
            )
        }
    }

    private fun validation(): Boolean {
        if (nama.isEmpty()) {
            message = "Nama tidak boleh kosong"
            return false
        } else if (nohp.isEmpty()) {
            message = "Nomor hp tidak boleh kosong"
            return false
        } else if (alamat.isEmpty()) {
            message = "Alamat tidak boleh kosong"
            return false
        } else if (pass.isEmpty()) {
            message = "Password tidak boleh kosong"
            return false
        } else if (email.isEmpty()) {
            message = "Email tidak boleh kosong"
            return false
        }
        return true
    }

    private fun getLatLong() {
        vProgress.visibility = View.VISIBLE
        Toast.makeText(applicationContext, "Sedang mencari koordinat...", Toast.LENGTH_SHORT).show()
        vLatitude.text = "Latitude: ${getLocation()!!.latitude}"
        vLongitude.text = "Longitude: ${getLocation()!!.longitude}"

        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(applicationContext)
            .load(
                "https://maps.geoapify.com/v1/staticmap?style=osm-carto&width=700&height=300&" +
                        "center=lonlat:${getLocation()!!.longitude},${getLocation()!!.latitude}&" +
                        "zoom=17.7327&marker=lonlat:${getLocation()!!.longitude},${getLocation()!!.latitude};" +
                        "type:material;color:%23ff3421;icontype:awesome&apiKey=a9b2423c37d747ddaef9fd169663b80a"
            )
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Toast.makeText(
                        applicationContext,
                        "Gagal mengatur koordinat, silahkan refresh",
                        Toast.LENGTH_LONG
                    ).show()
                    btnRegister.text = "Set Koordinat"
                    lytLL.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    Toast.makeText(
                        applicationContext,
                        "Sukses mengatur koordinat",
                        Toast.LENGTH_LONG
                    ).show()
                    enableRegister = true
                    btnRegister.text = "Daftar"
                    lytLL.visibility = View.VISIBLE
                    return false
                }

            })
            .placeholder(circularProgressDrawable)
            .error(R.drawable.refresh)
            .into(imgAddress)

        Handler().postDelayed({
            lytKoordinat.visibility = View.VISIBLE
            vProgress.visibility = View.GONE
        }, 1000)
    }

    private fun checkPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                perms
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        return true
    }

    private fun requestPermissions() {
        reqPermission.launch(perms)
    }

    private fun checkLocaitonEnbale(): Boolean {
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gps_enabled = false
        var network_enabled = false

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: java.lang.Exception) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: java.lang.Exception) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AppAlertDialog.show(
                supportFragmentManager,
                "Oops",
                "Mohon aktifkan akses lokasi Anda",
                error = true,
                title_positive = "OK",
                callbackPositive = {
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    reqGps.launch(intent)
                }
            )
            return false
        }
        return true
    }

    private fun getLocation(): Location? {
        try {
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

            // getting GPS status
            isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)

            // getting network status
            isNetworkEnabled = locationManager!!
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    //check the network permission
                    if (ActivityCompat.checkSelfPermission(
                            applicationContext,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            applicationContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ),
                            101
                        )
                    }
                    locationManager!!.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES.toLong(),
                        MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this
                    )
                    if (locationManager != null) {
                        loc =
                            locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        if (loc != null) {
                            lat = loc!!.getLatitude()
                            lng = loc!!.getLongitude()
                        }
                    }
                }

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (loc == null) {
                        //check the network permission
                        if (ActivityCompat.checkSelfPermission(
                                applicationContext,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                applicationContext,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            ActivityCompat.requestPermissions(
                                this, arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                ), 101
                            )
                        }
                        locationManager!!.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES.toLong(),
                            MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this
                        )
                        if (locationManager != null) {
                            loc = locationManager!!
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            if (loc != null) {
                                lat = loc!!.getLatitude()
                                lng = loc!!.getLongitude()
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return loc
    }


    override fun onLocationChanged(location: Location) {

    }
}