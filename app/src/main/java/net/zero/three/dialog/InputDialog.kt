package net.zero.three.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.dialog_input.*
import net.zero.three.R

class InputDialog: DialogFragment() {

    companion object {
        fun show(fm: FragmentManager, title: String = "", callback: (String) -> Unit) = InputDialog().apply {
            this.callback = callback
            this.title = title
        }.show(fm, "")
    }

    private lateinit var callback: (String) -> Unit
    var title = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_input, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (title.isNotEmpty()) vTitle.text = title

        btnOk.setOnClickListener {
            dismiss()
            callback.invoke(vValue.text.toString())
        }

        btnCancel.setOnClickListener {
            dismiss()
        }
    }
}