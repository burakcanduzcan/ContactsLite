package com.burakcanduzcan.contactslite.ui.groups

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        val adapter = GroupListAdapter(::clickItem, ::editItem, ::deleteItem)
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
            showAddGroupDialog()
        }
    }

    private fun clickItem(group: Group) {
        Toast.makeText(context, "Function not implemented. ${group.name}", Toast.LENGTH_SHORT)
            .show()
    }

    private fun editItem(group: Group) {
        showEditDialog(group)
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

    private fun showEditDialog(group: Group) {
        val bindingUpdateDialog = PopupGroupBinding.inflate(LayoutInflater.from(requireContext()))
        bindingUpdateDialog.etGroupName.setText(group.name)

        AlertDialog.Builder(requireContext())
            .setView(bindingUpdateDialog.root)
            .setTitle(getString(R.string.edit_group_name))
            .setPositiveButton(R.string.update) { _, _ ->
                if (bindingUpdateDialog.etGroupName.text.toString().isEmpty()) {
                    Snackbar.make(requireView(),
                        R.string.entered_group_name_cannot_be_blank,
                        Snackbar.LENGTH_SHORT).show()
                } else {
                    viewModel.editGroup(group, bindingUpdateDialog.etGroupName.text.toString())
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showAddGroupDialog() {
        val bindingAlertDialog = PopupGroupBinding.inflate(LayoutInflater.from(requireContext()))

        AlertDialog.Builder(requireContext())
            .setView(bindingAlertDialog.root)
            .setTitle(getString(R.string.add_new_group))
            .setPositiveButton(R.string.add) { _, _ ->
                if (bindingAlertDialog.etGroupName.text.toString().isEmpty()) {
                    Snackbar.make(requireView(),
                        R.string.entered_group_name_cannot_be_blank,
                        Snackbar.LENGTH_SHORT).show()
                } else {
                    viewModel.addNewGroup(bindingAlertDialog.etGroupName.text.toString())
                }
            }
            .show()
    }
}