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
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.android.synthetic.main.dialog_detail_order.*
import kotlinx.android.synthetic.main.dialog_detail_order.btnOrder
import kotlinx.android.synthetic.main.dialog_detail_order.footer
import kotlinx.android.synthetic.main.dialog_detail_order.vAlamat
import kotlinx.android.synthetic.main.dialog_detail_order.vAlamatLaundry
import kotlinx.android.synthetic.main.dialog_detail_order.vBerat
import kotlinx.android.synthetic.main.dialog_detail_order.vBiayaLayanan
import kotlinx.android.synthetic.main.dialog_detail_order.vCatatan
import kotlinx.android.synthetic.main.dialog_detail_order.vEmail
import kotlinx.android.synthetic.main.dialog_detail_order.vNama
import kotlinx.android.synthetic.main.dialog_detail_order.vNamaLaundry
import kotlinx.android.synthetic.main.dialog_detail_order.vNohp
import kotlinx.android.synthetic.main.dialog_detail_order.vNohpLaundry
import kotlinx.android.synthetic.main.dialog_detail_order.vToolbar
import kotlinx.android.synthetic.main.dialog_detail_order.vTotalAmount
import kotlinx.android.synthetic.main.dialog_detail_order.vTotalPembayaran
import net.zero.three.*
import net.zero.three.api.payload.response.ResDetailAkun
import net.zero.three.api.payload.response.ResOrder
import net.zero.three.api.payload.response.ResPayment
import net.zero.three.api.payload.response.ResStore
import net.zero.three.persistant.SessionManager
import net.zero.three.ui.MainActivity
import net.zero.three.viewmodel.AuthViewModel
import java.util.*

class DetailOrderDialog : DialogFragment() {

    companion object {
        fun show(
            fragmentManager: FragmentManager,
            orderId: String,
            fromOrder: Boolean? = false,
            callback: () -> Unit
        ) = DetailOrderDialog().apply {
            this.orderId = orderId
            this.fromOrder = fromOrder
            this.callback = callback
        }.show(fragmentManager, "")
    }

    lateinit var callback: () -> Unit
    private var orderId = ""
    var fromOrder: Boolean? = false
    private lateinit var _vm: AuthViewModel

    var paymentMethod = ""
    var totalAmount = ""
    var adminFee = ""
    var status_pengerjaan = ""

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
        _vm = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
    }

    private fun initUI() {
        if (fromOrder == false) {
            footer.visibility = View.VISIBLE
            vieww.visibility = View.VISIBLE
        }

        getDetailOrder()
    }

    private fun eventUI() {
        vToolbar.setNavigationOnClickListener {
            dismiss()
        }

        btnCopy.setOnClickListener {
            val kodePembayaran = vKodePembayaran.text.toString()
            copyClipBoard(requireContext(), kodePembayaran)
        }

        btnBatalkan.setOnClickListener {
            updateStatus(Progress.CANCEL.id.toString())
        }

        btnOrder.setOnClickListener {
            if (SessionManager.instance.level == "Merchant") {
                updateStatus((status_pengerjaan.toInt() + 1).toString())
            } else {
                PaymentMethodDialog.show(childFragmentManager) {
                    paymentMethod = it
                    pay()
                }
            }
        }
    }

    private fun updateStatus(statusPengerjaan: String) {
        LoadingDialog.show(childFragmentManager)
        _vm.updateStatus(orderId, statusPengerjaan)
            .observe(this, {
                LoadingDialog.close(childFragmentManager)
                when (it?.status) {
                    true -> {
                        AppAlertDialog.show(childFragmentManager,
                            "Sukses",
                            "Berhasil mengubah status",
                            callbackPositive = {
                                initUI()
                            })
                    }
                    false -> {
                        AppAlertDialog.show(
                            childFragmentManager,
                            "Oops",
                            it.message,
                            true
                        )
                    }
                }
            })
    }

    private fun getDetailOrder() {
        LoadingDialog.show(childFragmentManager)
        _vm.getDetailOrder(orderId).observe(viewLifecycleOwner, {
            LoadingDialog.close(childFragmentManager)
            when (it?.status) {
                true -> {
                    it.data?.let {
                        vNama.text = it.nama_pelanggan
                        vNohp.text = it.hp_pelanggan
                        vAlamat.text = it.alamat_pelanggan
                        vEmail.text = it.email_pelanggan

                        vNamaLaundry.text = it.store_name
                        vAlamatLaundry.text = it.alamat_laundry
                        vNohpLaundry.text = it.hp_laundry

                        vBerat.text = "${it.berat} kg"
                        vCatatan.text = it.catatan
                        vTotalAmount.text = it.sub_total.toDouble().toCurrency("Rp")

                        vOrderId.text = it.id_order
                        vInvoiceId.text = it.id_invoice
                        if (it.id_invoice.isNullOrEmpty()) vInvoiceId.text = "-"
                        if (it.status == Payment.PAID.id.toString()) {
                            vStatusPembayaran.text = "Lunas"
                            footer.visibility = View.GONE
                            lytKodePembayaran.visibility = View.GONE
                        } else if (it.status == Payment.FAILED.id.toString()) {
                            vStatusPembayaran.text = "Dibatalkan"
                            footer.visibility = View.GONE
                            lytKodePembayaran.visibility = View.GONE
                        } else {
                            vStatusPembayaran.text = "Belum dibayar"
                        }

                        vTgl.text =
                            it.created_at.convertDate("yyyy-mm-dd", "dd-mm-yyyy")

                        if (it.payment_method == PaymentMethod.BRIVA.name) {
                            vMetodePembayaran.text = "BRI Virtual Account"
                            vStatusPembayaran.text = "Menunggu Pembayaran"
                        } else if (it.payment_method == PaymentMethod.BCAVA.name) {
                            vMetodePembayaran.text = "BCA Virtual Account"
                            vStatusPembayaran.text = "Menunggu Pembayaran"
                        } else {
                            vMetodePembayaran.text = "Bayar Nanti"
                            lytKodePembayaran.visibility = View.GONE
                        }

                        if (!it.pay_code.isNullOrEmpty()) {
                            btnOrder.visibility = View.GONE
                        }

                        vKodePembayaran.text = it.pay_code
                        vBiayaLayanan.text = it.biaya_layanan
                        vTotalPembayaran.text = it.total_bayar.toDouble().toCurrency("Rp")

                        totalAmount = it.sub_total
                        adminFee = it.biaya_layanan
                        status_pengerjaan = it.status

                        if (SessionManager.instance.level == "Merchant") {
                            btnBatalkan.visibility = View.VISIBLE
                            btnOrder.visibility = View.VISIBLE
                            when (it.status) {
                                Progress.ORDER.id.toString() -> {
                                    btnOrder.text = "Menyetujui"
                                }
                                Progress.APPROVE.id.toString() -> {
                                    btnOrder.text = "Proses"
                                }
                                Progress.PROGRESS.id.toString() -> {
                                    btnOrder.text = "Selesaikan"
                                }
                                Progress.FINISH.id.toString() -> {
                                    btnOrder.visibility = View.GONE
                                }
                                Progress.CANCEL.id.toString() -> {
                                    btnBatalkan.visibility = View.GONE
                                }
                            }
                        }
                    }
                }
                false -> {
                    AppAlertDialog.show(
                        childFragmentManager,
                        "Oops",
                        it.message,
                        error = true,
                        callbackPositive = {
                            dismiss()
                        }
                    )
                }
            }
        })
    }

    private fun pay() {
        LoadingDialog.show(childFragmentManager)
        _vm.reqPayment(
            paymentMethod,
            totalAmount.toInt().toString(),
            adminFee.toInt().toString(),
            orderId,
            vNama.text.toString(),
            vEmail.text.toString(),
            vNohp.text.toString()
        ).observe(this, {
            LoadingDialog.close(childFragmentManager)
            when (it?.status) {
                true -> {
                    getDetailOrder()
                }
                false -> {
                    AppAlertDialog.show(
                        childFragmentManager,
                        "Oops",
                        it.message,
                        error = true
                    )
                }
            }
        })
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