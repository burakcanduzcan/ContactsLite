package com.burakcanduzcan.contactslite.ui.contacts

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.burakcanduzcan.contactslite.R
import com.burakcanduzcan.contactslite.data.entity.Contact
import com.burakcanduzcan.contactslite.databinding.ItemContactBinding

class ContactListAdapter(
    private val onContactClicked: (Contact) -> Unit,
    private val onContactLongClicked: (Contact) -> Unit
) : ListAdapter<Contact, ContactListAdapter.ContactListViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactListViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ItemContactBinding.inflate(from, parent, false)
        return ContactListViewHolder(parent.context, binding, onContactClicked, onContactLongClicked)
    }

    override fun onBindViewHolder(holder: ContactListViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class ContactListViewHolder(
        private val context: Context,
        private var binding: ItemContactBinding,
        private val onContactClicked: (Contact) -> Unit,
        private val onContactLongClicked: (Contact) -> Unit
        ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact) {
            binding.apply {
                tvName.text = contact.name
                tvSurname.text = contact.surname
                tvNumber.text = contact.number

                setStars(contact.groups.size)

                cl.setOnClickListener {
                    onContactClicked(contact)
                }
                cl.setOnLongClickListener {
                    onContactLongClicked(contact)
                    true
                }
            }
        }

        private fun setStars(groupCount: Int) {
            when (groupCount) {
                0 -> {
                    binding.ivStar1.setImageDrawable(AppCompatResources.getDrawable(context,
                        android.R.drawable.btn_star_big_off))
                }
                1 -> {
                    binding.ivStar1.setImageDrawable(AppCompatResources.getDrawable(context,
                        android.R.drawable.btn_star_big_on))
                }
                2 -> {
                    binding.ivStar1.setImageDrawable(AppCompatResources.getDrawable(context,
                        android.R.drawable.btn_star_big_on))
                    binding.ivStar2.setImageDrawable(AppCompatResources.getDrawable(context,
                        android.R.drawable.btn_star_big_on))
                }
                3 -> {
                    binding.ivStar1.setImageDrawable(AppCompatResources.getDrawable(context,
                        android.R.drawable.btn_star_big_on))
                    binding.ivStar2.setImageDrawable(AppCompatResources.getDrawable(context,
                        android.R.drawable.btn_star_big_on))
                    binding.ivStar3.setImageDrawable(AppCompatResources.getDrawable(context,
                        android.R.drawable.btn_star_big_on))
                }
                else -> {
                    binding.ivStar1.setImageDrawable(AppCompatResources.getDrawable(context,
                        android.R.drawable.btn_star_big_on))
                    binding.ivStar2.setImageDrawable(AppCompatResources.getDrawable(context,
                        android.R.drawable.btn_star_big_on))
                    binding.ivStar3.setImageDrawable(AppCompatResources.getDrawable(context,
                        android.R.drawable.btn_star_big_on))
                    binding.ivPlus.setImageDrawable(AppCompatResources.getDrawable(context,
                        R.drawable.ic_baseline_add_24))
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