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
import net.zero.three.R
import net.zero.three.api.payload.response.ResHistory
import net.zero.three.constants.Constants
import net.zero.three.convertDate
import net.zero.three.dialog.AppAlertDialog
import net.zero.three.dialog.LoadingDialog
import net.zero.three.toCurrency
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

        }
        vRecycler.layoutManager =
            LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        vRecycler.adapter = adapter

        LoadingDialog.show(supportFragmentManager)
        _vm.getHistory("0").observe(this, {
            LoadingDialog.close(supportFragmentManager)
            when (it?.status) {
                true -> {
                    it.data?.let {
                        dataHistory.addAll(it)
                        adapter.notifyDataSetChanged()
                    }
                }
                false -> {
                    AppAlertDialog.show(
                        supportFragmentManager,
                        "Oops",
                        "Terjadi kesalahan, coba lagi",
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

                val date = item.created_at.convertDate("yyyy-mm-dd", "dd-mm-yyyy", Locale("ID"))
                vTglPesanan.text = "Tanggal pesanan: $date"

                if (item.status_pembayaran == Constants.PAID.toString()) {
                    vStatusPembayaran.text = "Lunas"
                    imgStatus.setImageResource(R.drawable.paid)
                } else {
                    vStatusPembayaran.text = "Belum dibayar"
                    imgStatus.setImageResource(R.drawable.unpaid)
                }
            }
        }

    }
}