package com.burakcanduzcan.contactslite.ui.contacts

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.burakcanduzcan.contactslite.databinding.ItemContactBinding
import com.burakcanduzcan.contactslite.model.Contact

class ContactsAdapter(
    private val context: Context,
    private val list: ArrayList<Contact>,
    private val itemClick: (position: Int) -> Unit,
    private val itemLongClick: (position: Int) -> Unit,
) : RecyclerView.Adapter<ContactsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val from = LayoutInflater.from(context)
        val binding = ItemContactBinding.inflate(from, parent, false)
        return ContactsViewHolder(context, binding, itemClick, itemLongClick)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int = list.size
}