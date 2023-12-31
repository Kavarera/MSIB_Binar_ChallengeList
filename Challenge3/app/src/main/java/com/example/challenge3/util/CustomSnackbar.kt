package com.example.challenge3.util

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.example.challenge3.R
import com.example.challenge3.databinding.CustomSnackbarBinding
import com.google.android.material.snackbar.Snackbar

fun ShowSnackbarCustom(message:String?, title:String, type:Int, view:View){
    val snackbar = Snackbar.make(view,"",Snackbar.LENGTH_LONG)

    val customSnackbarBinding = snackbar.view as? Snackbar.SnackbarLayout
    customSnackbarBinding?.setPadding(0,0,0,0)

    when(type){
        400->{
            val binding = CustomSnackbarBinding.inflate(LayoutInflater.from(view.context))
            binding.ivLogoSnackbar.setImageResource(R.drawable.ic_baseline_close_24)
            binding.tvTitleSnackbar.text=title
            binding.tvTitleSnackbar.setTextColor(Color.WHITE)
            binding.tvDescriptionSnackbar.text=message
            binding.tvDescriptionSnackbar.setTextColor(Color.WHITE)

            val drawable = ContextCompat.getDrawable(view.context,R.drawable.shape_rounded_corner_edittext)

            drawable?.setColorFilter(Color.RED,PorterDuff.Mode.MULTIPLY)


            binding.mcvSnackbar.background=drawable
            snackbar.setBackgroundTint(Color.TRANSPARENT)


            customSnackbarBinding?.addView(binding.root)
        }
        200->{
            val binding = CustomSnackbarBinding.inflate(LayoutInflater.from(view.context))
            binding.ivLogoSnackbar.setImageResource(R.drawable.ic_baseline_check_24)
            binding.tvTitleSnackbar.text=title
            binding.tvTitleSnackbar.setTextColor(Color.WHITE)
            binding.tvDescriptionSnackbar.text=message
            binding.tvDescriptionSnackbar.setTextColor(Color.WHITE)

            val drawable = ContextCompat.getDrawable(view.context,R.drawable.shape_rounded_corner_edittext)

            drawable?.setColorFilter(Color.GREEN,PorterDuff.Mode.MULTIPLY)


            binding.mcvSnackbar.background=drawable
            snackbar.setBackgroundTint(Color.TRANSPARENT)


            customSnackbarBinding?.addView(binding.root)
        }
    }

    return snackbar.show()

}