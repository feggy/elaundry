package net.zero.three.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.dialog_payment_method.*
import net.zero.three.PaymentMethod
import net.zero.three.R
import javax.security.auth.callback.Callback

class PaymentMethodDialog : DialogFragment() {

    companion object {
        fun show(fragmentManager: FragmentManager, callback: (paymentMethod: String) -> Unit) =
            PaymentMethodDialog().apply {
                this.callback = callback
            }.show(fragmentManager, "")
    }

    lateinit var callback: (paymentMethod: String) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_payment_method, container, false)
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

        vToolbar.setNavigationOnClickListener {
            dismiss()
        }

        btnNanti.setOnClickListener {
            callback.invoke(PaymentMethod.NANTI.name)
            dismiss()
        }

        btnBri.setOnClickListener {
            callback.invoke(PaymentMethod.BRIVA.name)
            dismiss()
        }

        btnBca.setOnClickListener {
            callback.invoke(PaymentMethod.BCAVA.name)
            dismiss()

        }
    }
}