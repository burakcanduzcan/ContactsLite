package com.burakcanduzcan.contactslite.ui.contacts

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.burakcanduzcan.contactslite.R
import com.burakcanduzcan.contactslite.databinding.ItemContactBinding
import com.burakcanduzcan.contactslite.model.Contact

class ContactsViewHolder(
    private val context: Context,
    private val binding: ItemContactBinding,
    val itemClick: (position: Int) -> Unit,
    val itemLongClick: (position: Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.cl.setOnClickListener {
            itemClick(adapterPosition)
        }
        binding.cl.setOnLongClickListener {
            itemLongClick(adapterPosition)
            true
        }
    }

    fun bindData(contact: Contact) {
        binding.tvName.text = contact.name
        binding.tvSurname.text = contact.surname
        binding.tvNumber.text = contact.number
        setStars(contact.groups.size)
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