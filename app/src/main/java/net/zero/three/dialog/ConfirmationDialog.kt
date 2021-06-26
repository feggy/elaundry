package net.zero.three.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.dialog_confirmation.*
import net.zero.three.R

class ConfirmationDialog : DialogFragment() {

    companion object {
        fun show(
            fm: FragmentManager,
            title: String,
            description: String,
            title_positive: String = "",
            title_negative: String = "",
            callbackPositive: () -> Unit,
            callbackNegative: (() -> Unit)? = null
        ) = ConfirmationDialog().apply {
            this.title = title
            this.description = description
            this.title_positive = title_positive
            this.title_negative = title_negative
            this.callbackPositive = callbackPositive
            this.callbackNegative = callbackNegative
        }.show(fm, "")
    }

    private var title = ""
    private var description = ""
    private var title_positive = ""
    private var title_negative = ""

    lateinit var callbackPositive: () -> Unit
    var callbackNegative: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_confirmation, container, false)
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
        btnCancel?.text = if (title_negative == "") "Tidak" else title_negative
        btnOk?.text = if (title_positive == "") "Ya" else title_positive
    }

    private fun eventUI() {
        btnOk?.setOnClickListener {
            callbackPositive.invoke()
            dismiss()
        }

        btnCancel?.setOnClickListener {
            callbackNegative?.invoke()
            dismiss()
        }
    }
}