package com.burakcanduzcan.contactslite.ui.groups

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.burakcanduzcan.contactslite.data.entity.Group
import com.burakcanduzcan.contactslite.databinding.ItemGroupBinding

class GroupListAdapter(
    private val onGroupClicked: (Group) -> Unit,
    private val onGroupEditClicked: (Group) -> Unit,
    private val onGroupDeleteClicked: (Group) -> Unit,
) : ListAdapter<Group, GroupListAdapter.GroupListViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupListViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ItemGroupBinding.inflate(from, parent, false)
        return GroupListViewHolder(binding,
            onGroupClicked,
            onGroupEditClicked,
            onGroupDeleteClicked)
    }

    override fun onBindViewHolder(holder: GroupListViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class GroupListViewHolder(
        private val binding: ItemGroupBinding,
        private val onGroupClicked: (Group) -> Unit,
        private val onGroupEditClicked: (Group) -> Unit,
        private val onGroupDeleteClicked: (Group) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(group: Group) {
            binding.apply {
                tvGroupName.text = group.name
                tvGroupSize.text = "(${group.memberCount})"

                binding.cl.setOnClickListener {
                    onGroupClicked(group)
                }
                binding.ibRename.setOnClickListener {
                    onGroupEditClicked(group)
                }
                binding.ibDelete.setOnClickListener {
                    onGroupDeleteClicked(group)
                }
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Group>() {
            override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }
}