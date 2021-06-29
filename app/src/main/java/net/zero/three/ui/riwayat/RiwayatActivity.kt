package net.zero.three.ui.riwayat

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_riwayat.*
import kotlinx.android.synthetic.main.activity_riwayat.vRecycler
import kotlinx.android.synthetic.main.activity_riwayat.vToolbar
import net.zero.three.*
import net.zero.three.api.payload.response.ResHistory
import net.zero.three.constants.Constants
import net.zero.three.dialog.AppAlertDialog
import net.zero.three.dialog.DetailOrderDialog
import net.zero.three.dialog.LoadingDialog
import net.zero.three.ui.MainActivity
import net.zero.three.viewmodel.AuthViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RiwayatActivity : AppCompatActivity() {
    companion object {
        fun show(activity: Activity) {
            val i = Intent(activity, RiwayatActivity::class.java)
            activity.startActivity(i)
        }
    }

    lateinit var _vm: AuthViewModel

    val dataHistory = ArrayList<ResHistory>()
    lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat)

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
        adapter = HistoryAdapter(dataHistory) {
            DetailOrderDialog.show(supportFragmentManager, it.id) {}
        }
        vRecycler.layoutManager =
            LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        vRecycler.adapter = adapter

        LoadingDialog.show(supportFragmentManager)
        _vm.getHistory("").observe(this, {
            LoadingDialog.close(supportFragmentManager)
            when (it?.status) {
                true -> {
                    it.data?.let {
                        if (it.isNullOrEmpty()) {
                            lytNotFound.visibility = View.VISIBLE
                        }

                        dataHistory.addAll(it)
                        val dataSort = dataHistory.sortedByDescending { it.created_at }
                        dataHistory.clear()
                        dataHistory.addAll(dataSort)
                        adapter.notifyDataSetChanged()
                    }
                }
                false -> {
                    AppAlertDialog.show(
                        supportFragmentManager,
                        "Oops",
                        it.message,
                        error = true,
                        callbackPositive = {
                            MainActivity.show(this)
                        }
                    )
                }
            }
        })
    }

    private fun eventUI() {
        vToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    class HistoryAdapter(val data: ArrayList<ResHistory>, val callback: (ResHistory) -> Unit) :
        RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): HistoryAdapter.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.list_item_history, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int) {
            holder.bind(data[position], callback)
        }

        override fun getItemCount(): Int = data.size

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val vNamaStore: TextView = itemView.findViewById(R.id.vNamaStore)
            val vBerat: TextView = itemView.findViewById(R.id.vBerat)
            val vCatatan: TextView = itemView.findViewById(R.id.vCatatan)
            val vStatusPembayaran: TextView = itemView.findViewById(R.id.vStatusPembayaran)
            val vTotal: TextView = itemView.findViewById(R.id.vTotal)
            val vStatus: TextView = itemView.findViewById(R.id.vStatus)
            val vTglPesanan: TextView = itemView.findViewById(R.id.vTglPesanan)
            val imgStatus: ImageView = itemView.findViewById(R.id.imgStatus)
            val btnBayar: Button = itemView.findViewById(R.id.btnBayar)

            fun bind(item: ResHistory, callback: (ResHistory) -> Unit) {
                vNamaStore.text = item.store_name
                vBerat.text = "${item.berat} kg"
                vCatatan.text = item.catatan
                vTotal.text = item.amount.toDouble().toCurrency("Rp")

                val date = item.created_at.convertDate("yyyy-mm-dd", "dd-mm-yyyy")
                vTglPesanan.text = "Tanggal pesanan: $date"

                if (item.status_pengerjaan == Progress.APPROVE.name) {
                    vStatus.text = "Disetujui"
                } else if (item.status_pengerjaan == Progress.CANCEL.name) {
                    vStatus.text = "Dibatalkan"
                } else if (item.status_pengerjaan == Progress.FINISH.name) {
                    vStatus.text = "Selesai"
                } else if (item.status_pengerjaan == Progress.PROGRESS.name) {
                    vStatus.text = "Diproses"
                } else {
                    vStatus.text = "Menunggu"
                }

                if (item.status_pembayaran == Payment.PAID.id.toString()) {
                    vStatusPembayaran.text = "Lunas"
                    imgStatus.setImageResource(R.drawable.paid)
                    btnBayar.visibility = View.INVISIBLE
                } else if (item.status_pembayaran == Payment.FAILED.id.toString()) {
                    vStatusPembayaran.text = "Dibatalkan"
                    imgStatus.setImageResource(R.drawable.paid)
                    btnBayar.visibility = View.INVISIBLE
                } else {
                    vStatusPembayaran.text = "Belum dibayar"
                    imgStatus.setImageResource(R.drawable.unpaid)
                }

                itemView.setOnClickListener {
                    callback.invoke(item)
                }

                btnBayar.setOnClickListener {
                    callback.invoke(item)
                }
            }
        }

    }
}