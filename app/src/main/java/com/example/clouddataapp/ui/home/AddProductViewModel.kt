package com.example.clouddataapp.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.clouddataapp.models.Product
import com.example.clouddataapp.ui.home.HomeFragment.Companion.COLL_PRODUCTS
import com.google.firebase.firestore.FirebaseFirestore

class AddProductViewModel : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _isSaved = MutableLiveData(false)
    val isSaved: MutableLiveData<Boolean> = _isSaved

    fun saveProduct(
        db: FirebaseFirestore,
        name: String,
        price: String,
        brand: String,
        category: String,
        imgsrc: String
    ) {
        _isLoading.value = true

        if (!validateProduct(name, brand, category, price, imgsrc)) {
            _isLoading.value = false
            _isSaved.value = false
        } else {
            val product = Product(name, brand, category, imgsrc, price.toDouble())
            db.collection(COLL_PRODUCTS).add(product).addOnSuccessListener {
                _isLoading.value = false
                _isSaved.value = false
            }.addOnFailureListener {
                _isLoading.value = false
                _isSaved.value = false
            }.addOnCanceledListener {
                _isLoading.value = false
                _isSaved.value = false
            }
        }
    }

    private fun validateProduct(
        product: String,
        brand: String,
        category: String,
        price: String,
        imgsrc: String
    ): Boolean{
        return product.isNotEmpty() && brand.isNotEmpty() && category.isNotEmpty() && price.isNotEmpty() && imgsrc.isNotEmpty()
    }
}