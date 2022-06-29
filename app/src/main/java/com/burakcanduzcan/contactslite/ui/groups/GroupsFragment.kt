package com.burakcanduzcan.contactslite.ui.groups

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.burakcanduzcan.contactslite.ContactApplication
import com.burakcanduzcan.contactslite.R
import com.burakcanduzcan.contactslite.data.entity.Group
import com.burakcanduzcan.contactslite.databinding.FragmentGroupsBinding
import com.burakcanduzcan.contactslite.databinding.PopupGroupBinding
import com.burakcanduzcan.contactslite.ui.main.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class GroupsFragment : Fragment() {
    private lateinit var binding: FragmentGroupsBinding

    private val viewModel: GroupsViewModel by activityViewModels {
        GroupsViewModelFactory(
            (activity?.application as ContactApplication).database.groupDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentGroupsBinding.inflate(layoutInflater)

        binding.rvGroups.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val adapter = GroupListAdapter(::itemClick, ::editItem, ::deleteItem)
        binding.rvGroups.adapter = adapter

        viewModel.allGroups.observe(this.viewLifecycleOwner) { groups ->
            groups.let {
                adapter.submitList(it)
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).setFabVisibility(true)
        (requireActivity() as MainActivity).changeFabActionFromFragments {
            showAddOrEditDialog(true)
        }
    }

    private fun itemClick(group: Group) {
        /*
        todo: Group preview
         */
    }

    private fun editItem(group: Group) {
        showAddOrEditDialog(false, group)
    }

    private fun deleteItem(group: Group) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_group))
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                viewModel.deleteGroup(group)
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ ->
            }
            .show()
    }

    private fun showAddOrEditDialog(isActionInsertion: Boolean, group: Group? = null) {
        val bindingGroupDialog = PopupGroupBinding.inflate(LayoutInflater.from(requireContext()))
        val adb = AlertDialog.Builder(requireContext())
            .setView(bindingGroupDialog.root)

        if (isActionInsertion) {
            //true: insert
            adb.setTitle(getString(R.string.add_new_group))
                .setPositiveButton(R.string.add) { _, _ ->
                    if (bindingGroupDialog.etGroupName.text.toString().isEmpty()) {
                        Snackbar.make(requireView(),
                            R.string.entered_group_name_cannot_be_blank,
                            Snackbar.LENGTH_SHORT).show()
                    } else {
                        viewModel.addNewGroup(bindingGroupDialog.etGroupName.text.toString())
                    }
                }
                .setNegativeButton(R.string.cancel, null)
        } else {
            //false: update
            bindingGroupDialog.etGroupName.setText(group!!.name)

            adb.setTitle(getString(R.string.edit_group_name))
                .setPositiveButton(R.string.update) { _, _ ->
                    if (bindingGroupDialog.etGroupName.text.toString().isEmpty()) {
                        Snackbar.make(requireView(),
                            R.string.entered_group_name_cannot_be_blank,
                            Snackbar.LENGTH_SHORT).show()
                    } else {
                        viewModel.updateGroup(group, bindingGroupDialog.etGroupName.text.toString())
                    }
                }
                .setNegativeButton(R.string.cancel, null)
        }

        adb.show()
    }
}