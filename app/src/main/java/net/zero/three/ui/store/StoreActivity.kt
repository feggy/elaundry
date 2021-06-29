package net.zero.three.ui.store

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
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_store.*
import net.zero.three.R
import net.zero.three.api.payload.response.ResStore
import net.zero.three.dialog.AppAlertDialog
import net.zero.three.dialog.ConfirmationDialog
import net.zero.three.dialog.LoadingDialog
import net.zero.three.distanceInKm
import net.zero.three.ui.order.OrderActivity
import net.zero.three.viewmodel.AuthViewModel

class StoreActivity : AppCompatActivity() {

    companion object {
        fun show(activity: Activity) {
            val i = Intent(activity, StoreActivity::class.java)
            activity.startActivity(i)
        }
    }

    lateinit var _vm: AuthViewModel

    var myLat = ""
    var myLong = ""

    lateinit var adapterLaundryDistance: StoreAdapter
    var dataLaundryDistance = ArrayList<ResStore>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)
        
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
        getDetailAkun()

        adapterLaundryDistance = StoreAdapter(dataLaundryDistance) {
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
        vRecylerStore.layoutManager =
            LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        vRecylerStore.adapter = adapterLaundryDistance
        
    }

    private fun eventUI() {
        vToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun getDetailAkun() {
        LoadingDialog.show(supportFragmentManager)
        _vm.getDetailAkun().observe(this, {
            LoadingDialog.close(supportFragmentManager)
            when (it?.status) {
                true -> {
                    it.data?.let {

                        myLat = it.latitude
                        myLong = it.longitude

                        getStore(myLat, myLong)
                    }
                }
                false -> {

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
                            lytNotFound.visibility = View.VISIBLE
                        }
                        dataLaundryDistance.addAll(it)
                        adapterLaundryDistance.notifyDataSetChanged()
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

    class StoreAdapter(
        val data: ArrayList<ResStore>,
        val callback: (ResStore) -> Unit
    ) : RecyclerView.Adapter<StoreAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.list_item_store, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(data[position], callback)
        }

        override fun getItemCount(): Int = data.size

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val vImage: ImageView = itemView.findViewById(R.id.vImage)
            val vNamaToko: TextView = itemView.findViewById(R.id.vNamaToko)
            val vJarak: TextView = itemView.findViewById(R.id.vJarak)
            val vAlamatToko: TextView = itemView.findViewById(R.id.vAlamatToko)

            fun bind(item: ResStore, callback: (ResStore) -> Unit) {
                vNamaToko.text = item.store_name
                vJarak.text = "${item.distance.toDouble().distanceInKm()} km"
                vAlamatToko.text = item.address

                val circularProgressDrawable = CircularProgressDrawable(itemView.context)
                circularProgressDrawable.strokeWidth = 5f
                circularProgressDrawable.centerRadius = 30f
                circularProgressDrawable.start()

                Glide.with(itemView)
                    .load(item.image_store)
                    .placeholder(circularProgressDrawable)
                    .error(R.drawable.laundry_store)
                    .into(vImage)

                itemView.setOnClickListener {
                    callback.invoke(item)
                }
            }

        }

    }

    data class Store(
        val nama: String,
        var distance: Double,
        val image: String,
        val lat: String,
        val long: String,
        val alamat: String
    )
}