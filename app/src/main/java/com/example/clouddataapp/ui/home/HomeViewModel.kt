package com.example.clouddataapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.clouddataapp.models.Product
import com.example.clouddataapp.ui.home.HomeFragment.Companion.COLL_PRODUCTS
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

enum class ProductState{LOADING,SAVED,ERROR,NONE}
class HomeViewModel : ViewModel() {
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _selectedProduct = MutableLiveData<Product>()
    val selectedProduct: LiveData<Product> = _selectedProduct

    private val _saveState = MutableLiveData<ProductState>(ProductState.NONE)
    val saveState : LiveData<ProductState> = _saveState

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

    fun deleteProduct(db:FirebaseFirestore){
        db.collection(COLL_PRODUCTS).whereEqualTo("name",_selectedProduct.value?.name).get().addOnSuccessListener {
            if (!it.isEmpty){
                db.collection(COLL_PRODUCTS).document(it.documents[0].id).delete().addOnSuccessListener {
                    loadProducts(db)
                }
            }
        }
    }

    fun updateProduct(
        db: FirebaseFirestore,
        name:String,
        brand:String,
        category: String,
        price:String,
        imgsrc:String
    ){
        db.collection(COLL_PRODUCTS).whereEqualTo("name",_selectedProduct.value?.name).get().addOnSuccessListener {
            query -> if (query.isEmpty){
                _saveState.value = ProductState.ERROR
        }else{
            val product = query.documents[0].toObject(Product::class.java)
            product?.let {
                it.name = name
                it.brand = brand
                it.category = category
                it.price = price.toDouble()
                it.imgsrc= imgsrc
                db.collection(COLL_PRODUCTS).document(query.documents[0].id).set(it).addOnSuccessListener {
                    _saveState.value = ProductState.SAVED
                }.addOnFailureListener {
                    _saveState.value = ProductState.ERROR
                }
            }
        }
        }.addOnFailureListener {
            _saveState.value = ProductState.ERROR
        }
    }

    fun resetProductState(){
        _saveState.value = ProductState.NONE
    }
}



