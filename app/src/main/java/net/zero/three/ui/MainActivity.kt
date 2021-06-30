package net.zero.three.ui

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import kotlinx.android.synthetic.main.activity_main.*
import net.zero.three.*
import net.zero.three.api.payload.response.Antrian
import net.zero.three.api.payload.response.ResDetailAkun
import net.zero.three.api.payload.response.ResDetailOrder
import net.zero.three.api.payload.response.ResStore
import net.zero.three.dialog.AppAlertDialog
import net.zero.three.dialog.ConfirmationDialog
import net.zero.three.dialog.DetailOrderDialog
import net.zero.three.dialog.LoadingDialog
import net.zero.three.persistant.SessionManager
import net.zero.three.ui.login.LoginActivity
import net.zero.three.ui.order.OrderActivity
import net.zero.three.ui.profile.ProfileActivity
import net.zero.three.ui.riwayat.RiwayatActivity
import net.zero.three.ui.riwayat.RiwayatAntrianActivity
import net.zero.three.ui.settings.SettingsActivity
import net.zero.three.ui.splash.SplashActivity
import net.zero.three.ui.store.StoreActivity
import net.zero.three.ui.withdrawal.WithdrawalActivity
import net.zero.three.ui.withdrawal.WithdrawalHistoryActivity
import net.zero.three.viewmodel.AuthViewModel
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    companion object {
        fun show(activity: Activity) {
            val i = Intent(activity, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            activity.startActivity(i)
        }
    }

    lateinit var _vm: AuthViewModel

    var myLat: String = ""
    var myLong: String = ""

    lateinit var adapterLaundryDistance: LaundryDistanceAdapter
    lateinit var adapterAntrian: AntrianAdapter
    var dataLaundryDistance: ArrayList<ResStore> = ArrayList()
    var dataAntrian = ArrayList<Antrian>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    override fun onStart() {
        super.onStart()
        window.statusBarColor = resources.getColor(R.color.white)
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
        adapterLaundryDistance = LaundryDistanceAdapter(dataLaundryDistance) {
            ConfirmationDialog.show(
                supportFragmentManager,
                "Pesan Sekarang",
                "Ingin melakukan pemesanan sekarang atau butuh bantuan navigasi ke lokasi laundry?",
                "Pesan Sekarang",
                "Navigasi",
                callbackPositive = {
                    OrderActivity.show(this, it)
                }, callbackNegative = {
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data =
                        Uri.parse("http://maps.google.com/maps?saddr=$myLat,$myLong&daddr=${it.latitude},${it.longitude}")
                    startActivity(i)
                },
                close_button = true
            )
        }
        vRecyclerDistance.layoutManager =
            LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
        vRecyclerDistance.adapter = adapterLaundryDistance

        getDetailAkun()
    }

    private fun eventUI() {
        vAvatar.setOnClickListener {
            ProfileActivity.show(this)
        }

        btnUpgrade.setOnClickListener {
            val i = Intent(Intent.ACTION_DIAL)
            i.data = Uri.parse("tel:+6282283152687")
            startActivity(i)
        }

        btnOrder.setOnClickListener {
            StoreActivity.show(this)
        }

        btnSeeAllStore.setOnClickListener {
            StoreActivity.show(this)
        }

        btnHistory.setOnClickListener {
            RiwayatActivity.show(this)
        }

        btnAddOrder.setOnClickListener {
            StoreActivity.show(this)
        }

        btnHistory.setOnClickListener {
            RiwayatActivity.show(this)
        }

        vSettings.setOnClickListener {
            SettingsActivity.show(this)
        }

        btnWithdrawal.setOnClickListener {
            WithdrawalActivity.show(this)
        }

        btnHistoryPenarikan.setOnClickListener {
            WithdrawalHistoryActivity.show(this)
        }

        btnSeeAllAntrian.setOnClickListener {
            RiwayatAntrianActivity.show(this)
        }
    }

    private fun getDetailAkun() {
        LoadingDialog.show(supportFragmentManager)
        _vm.getDetailAkun().observe(this, {
            LoadingDialog.close(supportFragmentManager)
            when (it?.status) {
                true -> {
                    it.data?.let {
                        Glide.with(applicationContext)
                            .load(it.profile_photo_url)
                            .placeholder(R.drawable.avatar)
                            .error(R.drawable.avatar)
                            .into(vAvatar)

                        vNama.text = it.name
                        vLevel.text = it.level

                        if (it.level == "Merchant") {
                            lytSaldo.visibility = View.VISIBLE
                            vSaldo.text = it.saldo.toDouble().toCurrency("Rp")
                            lytWithdrawal.visibility = View.VISIBLE
                            lytPemesanan.visibility = View.GONE
                            vStoreDistance.visibility = View.GONE

                            /*val params = lytContentAntrian.layoutParams
                            params.width = RelativeLayout.LayoutParams.MATCH_PARENT
                            params.height = RelativeLayout.LayoutParams.MATCH_PARENT
                            lytContentAntrian.layoutParams = params*/

                            if (it.active == Status.REGISTER.id.toString()) {
                                btnUpgrade.visibility = View.VISIBLE
                            }
                        } else {
                            btnSeeAllAntrian.visibility = View.GONE
                        }

                        myLat = it.latitude
                        myLong = it.longitude

                        SessionManager.instance.hargaPerKg = it.laundry_per_kg
                        SessionManager.instance.biayaAdmin = it.fee
                        SessionManager.instance.userId = it.id
                        SessionManager.instance.level = it.level
                        SessionManager.instance.saldo = it.saldo

                        getStore(myLat, myLong)
                        loadAntrian()
                    }
                }
                false -> {
                    if (it.code.toString() == "501") {
                        AppAlertDialog.show(
                            supportFragmentManager,
                            "Oops",
                            "Sesi anda berakhir, silahkan login kembali",
                            true,
                            callbackPositive = {
                                SessionManager.instance.nohp = ""
                                SplashActivity.show(this)
                            }
                        )
                    }
                }
            }
        })
    }

    private fun getStore(lat: String, long: String) {
        _vm.getStore(true, lat, long).observe(this, {
            when (it?.status) {
                true -> {
                    it.data?.let {
                        if (it.isNullOrEmpty()) {
                            vStoreDistance.visibility = View.GONE
                        }

                        it.forEach {
                            val distance = SphericalUtil.computeDistanceBetween(
                                LatLng(
                                    lat.toDouble(),
                                    long.toDouble()
                                ), LatLng(it.latitude.toDouble(), it.longitude.toDouble())
                            ).distanceInKm()

                            dataLaundryDistance.add(
                                ResStore(
                                    it.id,
                                    it.store_name,
                                    it.address,
                                    it.latitude,
                                    it.longitude,
                                    it.image_store,
                                    it.distance.toDouble().distanceInKm().toString(),
                                    it.profile_photo_url,
                                    it.no_hp
                                )
                            )
                        }
//                        dataLaundryDistance.sortedByDescending { it.distance.toDouble() }
//                        val result = dataLaundryDistance.sortedBy { it.distance.toDouble() }
//                        dataLaundryDistance.clear()
//                        dataLaundryDistance.addAll(result)
                        adapterLaundryDistance.notifyDataSetChanged()
                    }
                }
                false -> {

                }
            }
        })
    }

    private fun loadAntrian() {
        adapterAntrian = AntrianAdapter(dataAntrian) {
            DetailOrderDialog.show(
                supportFragmentManager,
                it.orderId
            ) {
                val i = intent
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(i)
            }
        }
        vRecyclerAntrian.apply {
            adapter = adapterAntrian
            layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        }

        Timber.e("_ ${SessionManager.instance.level}")

        if (SessionManager.instance.level == "Merchant") {
            getDataAntrianMerchant()
        } else {
            getDataAntrianPelanggan()
        }
    }

    private fun getDataAntrianPelanggan() {
        dataAntrian.clear()
        LoadingDialog.show(supportFragmentManager)
        _vm.getHistory("").observe(this, {
            LoadingDialog.close(supportFragmentManager)
            when (it?.status) {
                true -> {
                    it.data?.let {
                        Timber.e("_Reshistory $it")
                        it.forEach {
                            if (it.status_pengerjaan != Progress.CANCEL.id.toString() || it.status_pengerjaan != Progress.FINISH.id.toString()) {
                                dataAntrian.add(
                                    Antrian(
                                        it.id,
                                        it.id_merchant,
                                        it.store_name,
                                        it.status_pembayaran,
                                        it.status_pengerjaan,
                                        it.created_at
                                    )
                                )
                            }
                        }
                        /*val sort = dataAntrian.sortedByDescending { it.created_at }
                        dataAntrian.clear()
                        dataAntrian.addAll(sort)*/
                        dataAntrian.sortedByDescending { it.created_at }
                        adapterAntrian.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    private fun getDataAntrianMerchant() {
        dataAntrian.clear()
        LoadingDialog.show(supportFragmentManager)
        _vm.getTransactionCustomer(SessionManager.instance.userId!!).observe(this, {
            LoadingDialog.close(supportFragmentManager)
            when (it?.status) {
                true -> {
                    it.data?.let {
                        if (it.isNullOrEmpty()) {
                            btnOrder.visibility = View.VISIBLE
                        } else {
                            it.forEach {
                                dataAntrian.add(
                                    Antrian(
                                        it.id_order,
                                        SessionManager.instance.userId!!,
                                        it.nama_pelanggan,
                                        it.hp_pelanggan,
                                        it.status,
                                        it.created_at
                                    )
                                )
                            }
                            val sort = dataAntrian.sortedByDescending { it.created_at }
                            dataAntrian.clear()
                            dataAntrian.addAll(sort)
                            adapterAntrian.notifyDataSetChanged()
                        }
                    }
                }
                false -> {

                }
            }
        })
    }

    class LaundryDistanceAdapter(
        val data: ArrayList<ResStore>,
        val callback: (ResStore) -> Unit
    ) : RecyclerView.Adapter<LaundryDistanceAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.list_item_laundry_distance, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(data[position], callback)
        }

        override fun getItemCount(): Int = data.size

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val imgStore: ImageView = itemView.findViewById(R.id.imgStore)
            val vNamaStore: TextView = itemView.findViewById(R.id.vNamaStore)
            val vDistance: TextView = itemView.findViewById(R.id.vDistance)

            fun bind(item: ResStore, callback: (ResStore) -> Unit) {
                vNamaStore.text = item.store_name
                vDistance.text = "${item.distance} km"

                val circularProgressDrawable = CircularProgressDrawable(itemView.context)
                circularProgressDrawable.strokeWidth = 5f
                circularProgressDrawable.centerRadius = 30f
                circularProgressDrawable.start()

                Glide.with(itemView)
                    .load(item.image_store)
                    .placeholder(circularProgressDrawable)
                    .error(R.drawable.laundry_store)
                    .into(imgStore)

                itemView.setOnClickListener {
                    callback.invoke(item)
                }
            }
        }
    }

    class AntrianAdapter(
        val data: ArrayList<Antrian>,
        val callback: (Antrian) -> Unit
    ) : RecyclerView.Adapter<AntrianAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.list_item_antrian_pelanggan, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(data[position], callback)
        }

        override fun getItemCount(): Int = data.size

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val vNama: TextView = itemView.findViewById(R.id.vNama)
            val vNohp: TextView = itemView.findViewById(R.id.vNohp)
            val vStatus: TextView = itemView.findViewById(R.id.vStatus)
            val vTgl: TextView = itemView.findViewById(R.id.vTgl)

            fun bind(item: Antrian, callback: (Antrian) -> Unit) {

                vNama.text = item.nama
                vNohp.text = item.no_hp

                when (item.status_pengerjaan) {
                    Progress.APPROVE.id.toString() -> {
                        vStatus.text = "Disetujui"
                    }
                    Progress.CANCEL.id.toString() -> {
                        vStatus.text = "Dibatalkan"
                    }
                    Progress.FINISH.id.toString() -> {
                        vStatus.text = "Selesai"
                    }
                    Progress.PROGRESS.id.toString() -> {
                        vStatus.text = "Diproses"
                    }
                    else -> {
                        vStatus.text = "Menunggu"
                    }
                }

                if (SessionManager.instance.level == "Pelanggan") {
                    when (item.no_hp) {
                        Payment.PAID.id.toString() -> {
                            vNohp.text = "Lunas"
                        }
                        Payment.FAILED.id.toString() -> {
                            vNohp.text = "Dibatalkan"
                        }
                        else -> {
                            vNohp.text = "Belum dibayar"
                        }
                    }
                } else {
                    if (item.status_pengerjaan == Progress.FINISH.id.toString() || item.status_pengerjaan == Progress.CANCEL.id.toString()) {
                        itemView.visibility = View.GONE
                        itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
                    }
                }

                vTgl.text = item.created_at.convertDate("yyyy-MM-dd'T'HH:mm", "dd-MM-yyyy HH:mm")

                itemView.setOnClickListener {
                    callback.invoke(item)
                }
            }
        }
    }
}