package com.example.packpals.views.packinglist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.packpals.R
import com.example.packpals.viewmodels.PackingListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PackingListFragment : Fragment() {
    private val viewModel: PackingListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_packing_list, container, false)
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchPackingList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //group list
        val groupListLayout = requireView().findViewById<LinearLayout>(R.id.groupLinearLayout)
        viewModel.packingList.observe(viewLifecycleOwner){ packingList ->
            groupListLayout.removeAllViews()
            for (item in packingList){
                if(item.group==true){
                    val itemView = LayoutInflater.from(context).inflate(R.layout.view_packing_list_item,groupListLayout,false)
                    val checkmarkImageView = itemView.findViewById<ImageView>(R.id.packingListItemCheckmarkIcon)
                    itemView.findViewById<TextView>(R.id.packingListItemName).text = item.title
                    if(item.packed ==true){
                        checkmarkImageView.setImageResource(R.drawable.ic_checked)
                    }
                    else{
                        checkmarkImageView.setImageResource(R.drawable.ic_unchecked)
                    }

                    itemView.setOnClickListener {
                        if (item != null) {
                            viewModel.editPacked(item)
                            viewModel.fetchPackingList()
                        }
                    }
                    groupListLayout.addView(itemView)
                    updateGroupPackingList()
                }
            }
        }

        val individualListLayout = requireView().findViewById<LinearLayout>(R.id.individualLinearLayout)
        viewModel.packingList.observe(viewLifecycleOwner) { packingList ->
            individualListLayout.removeAllViews()
            for (item in packingList){
                if(item.group == false){
                    val itemView = LayoutInflater.from(context).inflate(R.layout.view_packing_list_item,individualListLayout,false)
                    val checkmarkImageView = itemView.findViewById<ImageView>(R.id.packingListItemCheckmarkIcon)
                    itemView.findViewById<TextView>(R.id.packingListItemName).text = item.title
                    if(item.packed ==true){
                        checkmarkImageView.setImageResource(R.drawable.ic_checked)
                    }
                    else{
                        checkmarkImageView.setImageResource(R.drawable.ic_unchecked)
                    }

                    itemView.setOnClickListener {
                        if (item != null) {
                            viewModel.editPacked(item)
                            viewModel.fetchPackingList()
                        }
                    }
                    individualListLayout.addView(itemView)
                    updateIndividualPackingList()
                }
            }
        }

        val addGroupItemButton = requireView().findViewById<ImageButton>(R.id.addToGroupListButton)
        addGroupItemButton.setOnClickListener{
            val groupTextView = requireView().findViewById<EditText>(R.id.addNewGroupItem)
            if(groupTextView.text.toString()!=null){
                viewModel.createPackingListItem(groupTextView.text.toString(),true)
            }
            viewModel.fetchPackingList()

        }

        val addIndividualItemButton = requireView().findViewById<ImageButton>(R.id.addToMyListButton)
        addIndividualItemButton.setOnClickListener{
            val individualTextView = requireView().findViewById<EditText>(R.id.addNewIndividualItem)
            if(individualTextView.text.toString()!=null){
                viewModel.createPackingListItem(individualTextView.text.toString(),false)
            }
            viewModel.fetchPackingList()

        }
    }

    private fun updateGroupPackingList(){
        val groupListLayout = requireView().findViewById<LinearLayout>(R.id.groupLinearLayout)
        for(i in 0 until groupListLayout.childCount){
            val itemView = groupListLayout.getChildAt(i)
            val checkmarkImageView = itemView.findViewById<ImageView>(R.id.packingListItemCheckmarkIcon)
            val item = viewModel.packingList.value?.get(i)?.packed
            if(item == true){
                checkmarkImageView.setImageResource(R.drawable.ic_checked)
            }
            else{
                checkmarkImageView.setImageResource(R.drawable.ic_unchecked)
            }
        }
    }

    private fun updateIndividualPackingList(){
        val individualListLayout = requireView().findViewById<LinearLayout>(R.id.individualLinearLayout)
        for(i in 0 until individualListLayout.childCount){
            val itemView = individualListLayout.getChildAt(i)
            val checkmarkImageView = itemView.findViewById<ImageView>(R.id.packingListItemCheckmarkIcon)
            val item = viewModel.packingList.value?.get(i)?.packed
            if(item == true){
                checkmarkImageView.setImageResource(R.drawable.ic_checked)
            }
            else{
                checkmarkImageView.setImageResource(R.drawable.ic_unchecked)
            }
        }
    }
}