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
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import net.zero.three.R
import net.zero.three.dialog.LoadingDialog
import net.zero.three.ui.profile.ProfileActivity
import net.zero.three.viewmodel.AuthViewModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    companion object {
        fun show(activity: Activity) {
            val i = Intent(activity, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            activity.startActivity(i)
        }
    }

    lateinit var _vm: AuthViewModel

    var myLat = ""
    var myLong = ""

    lateinit var adapterLaundryDistance: LaundryDistanceAdapter
    var dataLaundryDistance = ArrayList<LaundryDistance>()

    val data = arrayListOf(
        LaundryDistance(
            "Hidup Baru Coin",
            0.0,
            "https://cutt.ly/pmedFOs",
            "-6.260849",
            "106.794295"
        ),
        LaundryDistance(
            "Fatmawati Laundry Amanah",
            0.0,
            "https://cutt.ly/qmedLd7",
            "-6.260457",
            "106.793307"
        ),
        LaundryDistance(
            "Professional Jakarta Laundry",
            0.0,
            "https://cutt.ly/MmedVmM",
            "-6.259833",
            "106.794404"
        ),
        LaundryDistance(
            "Speed Queen Laundry",
            0.0,
            "https://cutt.ly/ImedME5",
            "-6.260165",
            "106.796891"
        )
    )

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
        getDetailAkun()

    }

    private fun getDetailAkun() {
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

                        myLat = it.latitude
                        myLong = it.longitude

                        getDataDistance()

                        dataLaundryDistance.sortedByDescending { it.distance }

                        adapterLaundryDistance = LaundryDistanceAdapter(dataLaundryDistance) {
                            val i = Intent(Intent.ACTION_VIEW)
                            i.data =
                                Uri.parse("http://maps.google.com/maps?saddr=$myLat,$myLong&daddr=${it.lat},${it.long}")
                            startActivity(i)
                        }
                        vRecyclerDistance.layoutManager =
                            LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
                        vRecyclerDistance.adapter = adapterLaundryDistance
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

            dataLaundryDistance.add(LaundryDistance(it.nama, solution, it.image, it.lat, it.long))
        }
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
    }

    class LaundryDistanceAdapter(
        val data: ArrayList<LaundryDistance>,
        val callback: (LaundryDistance) -> Unit
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

            fun bind(item: LaundryDistance, callback: (LaundryDistance) -> Unit) {
                vNamaStore.text = item.nama
                vDistance.text = "${item.distance} km"

                val circularProgressDrawable = CircularProgressDrawable(itemView.context)
                circularProgressDrawable.strokeWidth = 5f
                circularProgressDrawable.centerRadius = 30f
                circularProgressDrawable.start()

                Glide.with(itemView)
                    .load(item.image)
                    .placeholder(circularProgressDrawable)
                    .error(R.drawable.laundry_store)
                    .into(imgStore)

                itemView.setOnClickListener {
                    callback.invoke(item)
                }
            }

        }

    }

    data class LaundryDistance(
        val nama: String,
        var distance: Double,
        val image: String,
        val lat: String,
        val long: String
    )
}