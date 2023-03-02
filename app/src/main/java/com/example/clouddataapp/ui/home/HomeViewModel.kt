package com.example.clouddataapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.clouddataapp.models.Product
import com.example.clouddataapp.ui.home.HomeFragment.Companion.COLL_PRODUCTS
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeViewModel : ViewModel() {
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _selectedProduct = MutableLiveData<Product>()
    val selectedProduct: LiveData<Product> = _selectedProduct

    fun getProducts(db: FirebaseFirestore) {
        loadProducts(db)
    }

    private fun loadProducts(db: FirebaseFirestore) {
        db.collection(COLL_PRODUCTS).get().addOnFailureListener {
            Log.e("HomeViewModel", "Error fetching products ${it.message}")
        }.addOnCanceledListener {
            Log.e("HomeViewModel", "Cancelled fetching products")
        }.addOnSuccessListener {
            val prds = it.toObjects(Product::class.java)
            _products.value = prds
            Log.d("HomeViewModel", "Products loaded ${prds.size}")
        }
    }
    fun setProduct(product: Product){
        _selectedProduct.value = product
    }
}



