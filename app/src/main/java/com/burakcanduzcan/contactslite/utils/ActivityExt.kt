package com.burakcanduzcan.contactslite.utils

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat

fun AppCompatActivity.getDrawableCompat(@DrawableRes resId: Int): Drawable =
    ResourcesCompat.getDrawable(resources, resId, null)!!