package net.zero.three.ui.withdrawal

import android.app.Activity
import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_withdrawal_history.*
import net.zero.three.R
import net.zero.three.Withdrawal
import net.zero.three.api.payload.response.ResWithdrawalHistory
import net.zero.three.convertDate
import net.zero.three.dialog.AppAlertDialog
import net.zero.three.dialog.ListUniversalDialog
import net.zero.three.dialog.LoadingDialog
import net.zero.three.persistant.SessionManager
import net.zero.three.viewmodel.AuthViewModel
import java.util.*
import kotlin.collections.ArrayList

class WithdrawalHistoryActivity : AppCompatActivity() {

    companion object {
        fun show(activity: Activity) {
            val i = Intent(activity, WithdrawalHistoryActivity::class.java)
            activity.startActivity(i)
        }
    }

    lateinit var _vm: AuthViewModel
    lateinit var adapter: HistoryAdapter
    val itemlist = ArrayList<ResWithdrawalHistory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_withdrawal_history)

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
        adapter = HistoryAdapter(itemlist)
        vRecycler.adapter = adapter
        vRecycler.layoutManager =
            LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)

        LoadingDialog.show(supportFragmentManager)
        _vm.getWithdrawalHistory(SessionManager.instance.userId!!).observe(this, {
            LoadingDialog.close(supportFragmentManager)
            when (it?.status) {
                true -> {
                    it.data?.let {
                        itemlist.addAll(it)
                        val sort = itemlist.sortedByDescending { it.created_at }
                        itemlist.clear()
                        itemlist.addAll(sort)
                        adapter.notifyDataSetChanged()
                    }
                }
                false -> {
                    AppAlertDialog.show(
                        supportFragmentManager,
                        "Oops",
                        "Terjadi suatu kesalahan",
                        true
                    )
                }
            }
        })
    }

    private fun eventUI() {
        vToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    class HistoryAdapter(val list: ArrayList<ResWithdrawalHistory>) :
        RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): HistoryAdapter.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.list_item_withdrawal, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int) {
            holder.bind(list[position])
        }

        override fun getItemCount(): Int = list.size

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val listBank = arrayListOf(
                ListUniversalDialog.Item(5, "BANK BCA"),
                ListUniversalDialog.Item(1, "BANK BRI"),
                ListUniversalDialog.Item(3, "BANK BNI"),
                ListUniversalDialog.Item(2, "BANK MANDIRI"),
                ListUniversalDialog.Item(4, "BANK SYARIAH INDONESIA"),
                ListUniversalDialog.Item(5, "PERMATA BANK")
            )

            val imgBank: ImageView = itemView.findViewById(R.id.imgBank)
            val vNama: TextView = itemView.findViewById(R.id.vNama)
            val vRek: TextView = itemView.findViewById(R.id.vRek)
            val vStatus: TextView = itemView.findViewById(R.id.vStatus)
            val vTgl: TextView = itemView.findViewById(R.id.vTgl)

            fun bind(item: ResWithdrawalHistory) {
                if (item.bankRek == "BANK BCA") {
                    imgBank.setImageResource(R.drawable.bca)
                } else if (item.bankRek == "BANK BRI") {
                    imgBank.setImageResource(R.drawable.bri)
                } else if (item.bankRek == "BANK BNI") {
                    imgBank.setImageResource(R.drawable.bni)
                } else if (item.bankRek == "BANK MANDIRI") {
                    imgBank.setImageResource(R.drawable.mandiri)
                } else if (item.bankRek == "BANK SYARIAH INDONESIA") {
                    imgBank.setImageResource(R.drawable.ic_bsi)
                } else if (item.bankRek == "PERMATA BANK") {
                    imgBank.setImageResource(R.drawable.permata)
                } else {
                    imgBank.scaleType = ImageView.ScaleType.CENTER_INSIDE
                }

                vNama.text = item.namaRek
                vRek.text = "${item.bankRek} - ${item.noRek}"

                if (item.status_withdrawl == Withdrawal.ACCEPTED.id.toString()) {
                    vStatus.text = Withdrawal.ACCEPTED.name
                } else if (item.status_withdrawl == Withdrawal.REJECTED.id.toString()) {
                    vStatus.text = Withdrawal.REJECTED.name
                } else {
                    vStatus.text = Withdrawal.REQUEST.name
                }

                vTgl.text = item.created_at.convertDate("yyyy-mm-dd", "dd-mm-yyyy", Locale("ID"))
            }

        }

    }
}