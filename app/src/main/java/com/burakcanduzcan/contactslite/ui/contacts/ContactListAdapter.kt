package com.burakcanduzcan.contactslite.ui.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.burakcanduzcan.contactslite.data.entity.Contact
import com.burakcanduzcan.contactslite.databinding.ItemContactBinding

class ContactListAdapter(
    private val onContactClicked: (Contact) -> Unit,
    private val contactEditButtonClick: (Contact) -> Unit,
    private val contactStarButtonClick: (Contact) -> Unit,
    private val contactAddToQuickCallButtonClick: (Contact) -> Unit,
    private val contactDeleteButtonClick: (Contact) -> Unit,
) : ListAdapter<Contact, ContactListAdapter.ContactListViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactListViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ItemContactBinding.inflate(from, parent, false)
        return ContactListViewHolder(
            binding,
            onContactClicked,
            contactEditButtonClick,
            contactStarButtonClick,
            contactAddToQuickCallButtonClick,
            contactDeleteButtonClick)
    }

    override fun onBindViewHolder(holder: ContactListViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class ContactListViewHolder(
        private var binding: ItemContactBinding,
        private val onContactClicked: (Contact) -> Unit,
        private val contactEditButtonClick: (Contact) -> Unit,
        private val contactStarButtonClick: (Contact) -> Unit,
        private val contactAddToQuickCallButtonClick: (Contact) -> Unit,
        private val contactDeleteButtonClick: (Contact) -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact) {
            binding.apply {
                if (contact.name.length > 20) {
                    tvName.text = "contact.name.take(20)..."
                } else {
                    tvName.text = contact.name
                }
                tvNumber.text = "+${contact.countryCode}${contact.number}"
                cl.setOnClickListener {
                    onContactClicked(contact)
                }
                ibRename.setOnClickListener {
                    contactEditButtonClick(contact)
                }
                ibStar.setOnClickListener {
                    contactStarButtonClick(contact)
                }
                ibAddToQuickCall.setOnClickListener {
                    contactAddToQuickCallButtonClick(contact)
                }
                ibDelete.setOnClickListener {
                    contactDeleteButtonClick(contact)
                }
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Contact>() {
            override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
                return oldItem.number == newItem.number
            }
        }
    }
}