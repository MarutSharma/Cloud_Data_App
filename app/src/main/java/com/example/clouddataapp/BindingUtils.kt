package com.example.clouddataapp

import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import coil.load
import com.example.clouddataapp.ui.home.ProductState

@BindingAdapter("load_image")
fun ImageView.loadImage(url:String?){
    url.let {
        load(it)
    }
}

@BindingAdapter("set_visibility")
fun ProgressBar.setVisibility(isVisible:Boolean){
    visibility = if (isVisible){
        ProgressBar.VISIBLE
    }else{
        ProgressBar.GONE
    }
}

@BindingAdapter("set_save_state")
fun ProgressBar.setVisibility(state: ProductState){
    visibility = if (state==ProductState.LOADING){
        ProgressBar.VISIBLE
    }else{
        ProgressBar.GONE
    }
}