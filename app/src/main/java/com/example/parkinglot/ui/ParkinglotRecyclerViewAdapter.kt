package com.example.parkinglot

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.parkinglot.data.DataParkinglot
import com.example.parkinglot.databinding.ParkinglotListItemBinding
import com.example.parkinglot.placeholder.PlaceholderContent

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 */
class ParkinglotRecyclerViewAdapter(
    private var values: List<DataParkinglot> = ArrayList()
) : RecyclerView.Adapter<ParkinglotRecyclerViewAdapter.ViewHolder>() {
    
    private var listener: OnClickListener? = null

    // A function to bind the onclickListener.
    fun setOnClickListener(listener: OnClickListener) {
        this.listener = listener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(position: Int, model: DataParkinglot)
        fun onLongClick(position: Int, model: DataParkinglot)
        fun onClickToEdit(position: Int, model: DataParkinglot)
        fun onClickToDelete(position: Int, model: DataParkinglot)
    }

    fun submitList(d: List<DataParkinglot>) {
//        values.clear()
//        values.addAll(d)
        values = d
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ParkinglotListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        val cpi = PlaceholderContent.createPlaceholderItem(position, item)
        holder.idView.text = cpi.id
        holder.contentView.text = cpi.content
        holder.btnEditView.setOnClickListener({
            if (listener != null) {
                listener!!.onClickToEdit(position, item )
            }
        })
        holder.btnDeleteView.setOnClickListener({
            if (listener != null) {
                listener!!.onClickToDelete(position, item )
            }
        })
        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onClick(position, item )
            }
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: ParkinglotListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content
        val btnEditView: TextView = binding.buttonEdit
        val btnDeleteView: TextView = binding.buttonDelete


        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}