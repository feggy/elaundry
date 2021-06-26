package net.zero.three.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.dialog_list_universal.*
import net.zero.three.R

class ListUniversalDialog : DialogFragment() {
    companion object {
        fun show(
            fragmentManager: FragmentManager,
            data: ArrayList<Item>,
            title: String = "",
            callback: (Item) -> Unit
        ) =
            ListUniversalDialog().apply {
                this.data = data
                this.title = title
                this.callback = callback
            }.show(fragmentManager, "")
    }

    private var data = ArrayList<Item>()
    private var tempData = ArrayList<Item>()
    lateinit var callback: (Item) -> Unit
    lateinit var adapter: UniversalAdapter
    private var title = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_list_universal, container, false)
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
        tempData.addAll(data)
        adapter = UniversalAdapter(data) {
            callback.invoke(it)
            dismiss()
        }
        vRecycler.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        vRecycler.adapter = adapter
    }

    private fun eventUI() {
    }

    class UniversalAdapter(val data: ArrayList<Item>, val callback: (Item) -> Unit) :
        RecyclerView.Adapter<UniversalAdapter.ViewHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): UniversalAdapter.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.list_item_list_universal, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(data[position], callback)
        }

        override fun getItemCount(): Int = data.size

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val vItem: TextView = itemView.findViewById(R.id.vItem)

            fun bind(item: Item, callback: (Item) -> Unit) {
                vItem.text = item.name

                itemView.setOnClickListener {
                    callback.invoke(item)
                }
            }

        }
    }

    data class Item(val id: Int? = null, val name: String)
}