package com.test.postsmobiletest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.github.pavlospt.roundedletterview.RoundedLetterView
import com.test.postsmobiletest.R
import com.test.postsmobiletest.model.Post

class ContactPhoneAdapter(val context: Context, private val contactList: MutableList<String>) : RecyclerView.Adapter<ContactPhoneAdapter.ContactViewHolder>() {

    var contactPhone = mutableListOf<String>()
    var onItemClick: ((String)->Unit) ?= null

    fun setContactList() {
        this.contactPhone = contactList
    }

    fun removeAt(position: Int): Int {
        contactList.removeAt(position)
        notifyItemRemoved(position)
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_contact_phone,
                parent,
                false
            )
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contactPhone = contactList[position]
        holder.tv_display.text = contactPhone
        holder.r_letter.titleText = contactPhone[0].toString()

/*
        holder.iv_delete.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete")
            builder.setMessage("Are you sure to delete?")
            builder.setIcon(R.drawable.ic_warning)

            builder.setPositiveButton("DELETE"){ _, _ ->
                deleteItem(position)
            }

            builder.setNegativeButton("CANCEL"){ dialog, _ ->
                dialog.dismiss()
            }
            builder.create()
            builder.setCancelable(false)
            builder.show()
        }
*/

    }

    inner class ContactViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val tv_display: TextView = itemView!!.findViewById(R.id.tv_display)
        val r_letter: RoundedLetterView = itemView!!.findViewById(R.id.r_letter)

        init {
            itemView?.setOnClickListener {
                onItemClick?.invoke(contactList[adapterPosition])
            }
        }
    }

    override fun getItemCount(): Int {
        return contactPhone.size
    }

       fun deleteItem(it: String){
        contactList.remove(it)
    }
}