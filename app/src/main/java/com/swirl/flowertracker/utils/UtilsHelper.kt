package com.swirl.flowertracker.utils

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.provider.Settings

fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.fromParts("package", context.packageName, null)
    context.startActivity(intent)
}

fun Int.dpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}