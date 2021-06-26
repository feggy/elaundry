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
import net.zero.three.dialog.ConfirmationDialog
import net.zero.three.dialog.LoadingDialog
import net.zero.three.ui.MainActivity
import net.zero.three.ui.order.OrderActivity
import net.zero.three.viewmodel.AuthViewModel

class StoreActivity : AppCompatActivity() {

    companion object {
        fun show(activity: Activity) {
            val i = Intent(activity, StoreActivity::class.java)
            activity.startActivity(i)
        }
    }

    val data = arrayListOf(
        Store(
            "Hidup Baru Coin",
            0.0,
            "https://cutt.ly/pmedFOs",
            "-6.260849",
            "106.794295",
            "Jl. Hidup Baru 2-11, RW.6, Gandaria Utara, Kec. Kby. Baru, Kota Jakarta Selatan, Daerah Khusus Ibukota Jakarta 12140"
        ),
        Store(
            "Fatmawati Laundry Amanah",
            0.0,
            "https://cutt.ly/qmedLd7",
            "-6.260457",
            "106.793307",
            "Jl. Karya Utama No.3, RT.11/RW.3, Gandaria Utara, Kec. Kby. Baru, Kota Jakarta Selatan, Daerah Khusus Ibukota Jakarta 12140"
        ),
        Store(
            "Professional Jakarta Laundry",
            0.0,
            "https://cutt.ly/MmedVmM",
            "-6.259833",
            "106.794404",
            "RT.7/RW.6, Gandaria Utara, Kec. Kby. Baru, Kota Jakarta Selatan, Daerah Khusus Ibukota Jakarta"
        ),
        Store(
            "Speed Queen Laundry",
            0.0,
            "https://cutt.ly/ImedME5",
            "-6.260165",
            "106.796891",
            "Jl. Damai Raya, RT.6/RW.5, Cipete Utara, Kec. Kby. Baru, Kota Jakarta Selatan, Daerah Khusus Ibukota Jakarta 12150"
        )
    )

    lateinit var _vm: AuthViewModel

    var myLat = ""
    var myLong = ""

    lateinit var adapterLaundryDistance: StoreAdapter
    var dataLaundryDistance = ArrayList<Store>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)
        
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
        getDetailAkun()
        
    }

    private fun eventUI() {
        
    }

    private fun getDetailAkun() {
        LoadingDialog.show(supportFragmentManager)
        _vm.getDetailAkun().observe(this, {
            LoadingDialog.close(supportFragmentManager)
            when (it?.status) {
                true -> {
                    it.data?.get(0)?.let {

                        myLat = it.latitude
                        myLong = it.longitude

                        getDataDistance()

                        dataLaundryDistance.sortedByDescending { it.distance }

                        adapterLaundryDistance = StoreAdapter(dataLaundryDistance) {
                                ConfirmationDialog.show(
                                    supportFragmentManager,
                                    "Pesan Sekarang",
                                    "Ingin melakukan pemesanan sekarang atau butuh bantuan navigasi ke lokasi laundry?",
                                    "Pesan Sekarang",
                                    "Navigasi",
                                    callbackPositive = {
                                        OrderActivity.show(this)
                                    }, callbackNegative = {
                                        val i = Intent(Intent.ACTION_VIEW)
                                        i.data =
                                            Uri.parse("http://maps.google.com/maps?saddr=$myLat,$myLong&daddr=${it.lat},${it.long}")
                                        startActivity(i)
                                    },
                                    close_button = true
                                )
                            }
                        vRecylerStore.layoutManager =
                            LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
                        vRecylerStore.adapter = adapterLaundryDistance
                    }
                }
                false -> {

                }
            }
        })
    }

    private fun getDataDistance() {
        data.forEach {
            val loc1 = Location("")
            loc1.latitude = myLat.toDouble()
            loc1.longitude = myLong.toDouble()

            val loc2 = Location("")
            loc2.latitude = it.lat.toDouble()
            loc2.longitude = it.long.toDouble()

            val distanceInMeters = loc1.distanceTo(loc2)

            val distanceInKm = distanceInMeters * 0.001
            val number3digits: Double = Math.round(distanceInKm * 1000.0) / 1000.0
            val number2digits: Double = Math.round(number3digits * 100.0) / 100.0
            val solution: Double = Math.round(number2digits * 10.0) / 10.0

            dataLaundryDistance.add(
                Store(
                    it.nama,
                    solution,
                    it.image,
                    it.lat,
                    it.long,
                    it.alamat
                )
            )
        }
    }

    class StoreAdapter(
        val data: ArrayList<Store>,
        val callback: (Store) -> Unit
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

            fun bind(item: Store, callback: (Store) -> Unit) {
                vNamaToko.text = item.nama
                vJarak.text = "${item.distance} km"
                vAlamatToko.text = item.alamat

                val circularProgressDrawable = CircularProgressDrawable(itemView.context)
                circularProgressDrawable.strokeWidth = 5f
                circularProgressDrawable.centerRadius = 30f
                circularProgressDrawable.start()

                Glide.with(itemView)
                    .load(item.image)
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