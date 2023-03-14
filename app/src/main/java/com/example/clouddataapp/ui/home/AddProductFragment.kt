package com.example.clouddataapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.clouddataapp.R
import com.example.clouddataapp.databinding.FragmentAddProductBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddProductFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore
    private val viewModel: AddProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Firebase.firestore
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_product, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        viewModel.isSaved.observe(viewLifecycleOwner) { state ->
            if (state) {
                findNavController().navigateUp()
            } else {
                binding.containerBox.isEnabled = true
                binding.fabSave.text = "Save"
            }
        }

        binding.apply {
            fabSave.setOnClickListener {
                containerBox.isEnabled = false
                val name = editPrdName.text.toString().trim()
                val price = editPrdPrice.text.toString().trim()
                val brand = editPrdBrand.text.toString().trim()
                val category = editPrdCategory.text.toString().trim()
                val imgsrc = editPrdUrl.text.toString().trim()
                fabSave.text = "Saving..."
                viewModel.saveProduct(db, name, price, brand, category, imgsrc)
            }
        }
    }
}