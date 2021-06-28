package net.zero.three.dialog

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.dialog_detail_order.*
import kotlinx.android.synthetic.main.dialog_detail_order.vAlamat
import kotlinx.android.synthetic.main.dialog_detail_order.vAlamatLaundry
import kotlinx.android.synthetic.main.dialog_detail_order.vEmail
import kotlinx.android.synthetic.main.dialog_detail_order.vNama
import kotlinx.android.synthetic.main.dialog_detail_order.vNamaLaundry
import kotlinx.android.synthetic.main.dialog_detail_order.vNohp
import kotlinx.android.synthetic.main.dialog_detail_order.vNohpLaundry
import net.zero.three.*
import net.zero.three.api.payload.response.ResDetailAkun
import net.zero.three.api.payload.response.ResOrder
import net.zero.three.api.payload.response.ResPayment
import net.zero.three.api.payload.response.ResStore
import java.util.*

class DetailOrderDialog : DialogFragment() {

    companion object {
        fun show(
            fragmentManager: FragmentManager,
            resPayment: ResPayment? = null,
            resOrder: ResOrder? = null,
            resStore: ResStore? = null,
            resDetailAkun: ResDetailAkun? = null,
            callback: () -> Unit
        ) = DetailOrderDialog().apply {
            this.resPayment = resPayment
            this.resOrder = resOrder
            this.resStore = resStore
            this.resDetailAkun = resDetailAkun
            this.callback = callback
        }.show(fragmentManager, "")
    }

    var resDetailAkun: ResDetailAkun? = null
    var resPayment: ResPayment? = null
    var resOrder: ResOrder? = null
    var resStore: ResStore? = null
    lateinit var callback: () -> Unit

    var total = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_detail_order, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.statusBarColor = resources.getColor(R.color.status_bar_color)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogFullScreen)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
    }

    private fun init() {
        initData()
        initUI()
        eventUI()
    }

    private fun initData() {

    }

    private fun initUI() {
        resDetailAkun?.let {
            vNama.text = it.name
            vNohp.text = it.no_hp
            vAlamat.text = it.address
            vEmail.text = it.email
        }

        resStore?.let {
            vNamaLaundry.text = it.store_name
            vAlamatLaundry.text = it.address
            vNohpLaundry.text = it.no_hp
        }

        resOrder?.let {
            vBerat.text = "${it.berat} kg"
            vCatatan.text = ""
            total = it.amount_satuan.toInt()*it.berat.toDouble().toInt()
            vTotalAmount.text = total.toDouble().toCurrency("Rp")
        }

        resPayment?.let {
            vOrderId.text = it.id_order
            vInvoiceId.text = it.id_invoice
            if (it.status == Payment.PAID.id.toString()) {
                vStatusPembayaran.text = "Lunas"
                footer.visibility = View.GONE
                lytKodePembayaran.visibility = View.GONE
            } else {
                vStatusPembayaran.text = "Belum dibayar"
            }

            vTgl.text = it.created_at.convertDate("yyyy-mm-dd", "dd-mm-yyyy", Locale("ID"))

            if (it.payment_method == PaymentMethod.BRIVA.name) {
                vMetodePembayaran.text ="BRI Virtual Account"
            } else if (it.payment_method == PaymentMethod.BCAVA.name) {
                vMetodePembayaran.text = "BCA Virtual Account"
            } else {
                vMetodePembayaran.text = "Bayar Nanti"
                lytKodePembayaran.visibility = View.GONE
            }

            vKodePembayaran.text = it.pay_code
            vBiayaLayanan.text = ""
            vTotalPembayaran.text = it.amount
        }
    }

    private fun eventUI() {
        vToolbar.setNavigationOnClickListener {
            dismiss()
        }

        btnCopy.setOnClickListener {
            val kodePembayaran = vKodePembayaran.text.toString()
            copyClipBoard(requireContext(), kodePembayaran)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        callback.invoke()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)

        callback.invoke()
    }
}