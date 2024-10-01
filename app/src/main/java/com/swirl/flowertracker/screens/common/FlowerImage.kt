package com.swirl.flowertracker.screens.common

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.swirl.flowertracker.R

@Composable
fun FlowerImage(
    modifier: Modifier = Modifier
        .size(128.dp)
        .padding(bottom = 16.dp),
    imageUri: String? = null,
    contentScale: ContentScale = ContentScale.Inside
) {
    val img = imageUri?.let { Uri.parse(it) } ?: R.drawable.placeholder
    val painter = rememberAsyncImagePainter(img)

    Image(
        painter = painter,
        contentDescription = stringResource(R.string.desc_flower_image),
        contentScale = contentScale,
        modifier = modifier
    )
}
