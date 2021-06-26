package net.zero.three.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.dialog_alert.*
import kotlinx.android.synthetic.main.dialog_alert.titleTxt
import net.zero.three.R

class AppAlertDialog : DialogFragment() {

    companion object {
        fun show(
            fm: FragmentManager,
            title: String,
            description: String,
            error: Boolean = false,
            info: Boolean = false,
            title_positive: String? = null,
            callbackPositive: (() -> Unit)? = null,
        ) = AppAlertDialog().apply {
            this.title = title
            this.description = description
            this.title_positive = title_positive
            this.error = error
            this.info = info
            this.callbackPositive = callbackPositive
        }.show(fm, "")
    }

    private var title = ""
    private var description = ""
    private var title_positive: String? = null
    private var error = false
    private var info = false

    var callbackPositive: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_alert, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
    }

    private fun init() {
        initUI()
        eventUI()
    }

    private fun initUI() {
        titleTxt?.text = title
        contentTxt?.text = description
        title_positive?.let {
            submitBtn?.text = title_positive
        }

        if (error) {
            circleLayout?.setBackgroundResource(R.drawable.circle_orange)
            statusIconImg?.setImageResource(R.drawable.ic_close)
        }

        if (info) {
            circleLayout?.setBackgroundResource(R.drawable.ic_circle_info)
        }
    }

    private fun eventUI() {
        submitBtn?.setOnClickListener {
            callbackPositive?.invoke()
            dismiss()
        }
    }
}