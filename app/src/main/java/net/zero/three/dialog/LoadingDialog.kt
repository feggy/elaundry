package net.zero.three.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import net.zero.three.R

class LoadingDialog: DialogFragment() {

    companion object{
        fun show(fm: FragmentManager) = LoadingDialog().apply {
            this.fm = fm
        }.show(fm, "loading")
        fun close(fm: FragmentManager) = LoadingDialog().apply {
            this.fm = fm
        }.close()
    }

    lateinit var fm: FragmentManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_loading, container, false)
    }

    fun close() {
        val preview: Fragment? = fm.findFragmentByTag("loading")
        preview?.let {
            val df: DialogFragment = it as DialogFragment
            df.dismiss()
        }
    }
}