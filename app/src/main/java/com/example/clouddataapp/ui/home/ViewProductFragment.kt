package com.example.clouddataapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.clouddataapp.R
import com.example.clouddataapp.databinding.FragmentViewProductBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ViewProductFragment : Fragment() {

    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var db: FirebaseFirestore
    private lateinit var _binding: FragmentViewProductBinding
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Firebase.firestore
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_view_product, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.resetProductState()
        binding.fabEdit.setOnClickListener {
            findNavController().navigate(R.id.action_viewProductFragment_to_editProductFragment)
        }
        binding.ivMore.setOnClickListener {
            val menu = PopupMenu(requireContext(),it)
            menu.inflate(R.menu.product_popup_menu)
            menu.setOnMenuItemClickListener {item ->
                when(item.itemId){
                    R.id.action_delete ->{
                        viewModel.deleteProduct(db)
                        findNavController().navigateUp()
                        true
                    }
                    R.id.action_report ->{
                        true
                    }
                    else -> false
                }
            }
            menu.show()
        }
    }
}