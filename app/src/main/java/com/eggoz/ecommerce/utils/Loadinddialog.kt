package com.eggoz.ecommerce.utils

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import com.eggoz.ecommerce.R


class Loadinddialog {
    private var dialogLoader:Dialog? =null

        fun create(context:Context){
            dialogLoader = Dialog(context)
            dialogLoader?.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialogLoader?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialogLoader?.setCancelable(false)
            dialogLoader?.setContentView(R.layout.layout_loader)
            val imageViewAnimation: ImageView = dialogLoader?.findViewById(R.id.animate_icon)!!
            val rotate = RotateAnimation(
                0F, 180F, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f
            )
            rotate.duration = 2000
            rotate.interpolator = LinearInterpolator()
            imageViewAnimation.startAnimation(rotate)
            dialogLoader!!.show()
        }
    fun dismiss(){
        dialogLoader?.dismiss()
    }
    fun isShowing():Boolean{
        return dialogLoader?.isShowing ?:false
    }

}