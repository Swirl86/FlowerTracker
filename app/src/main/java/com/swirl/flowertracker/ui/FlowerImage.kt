package com.swirl.flowertracker.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.swirl.flowertracker.R

@Composable
fun FlowerImage(imageUrl: String? = null, modifier: Modifier = Modifier
    .size(128.dp)
    .padding(bottom = 16.dp)
) {
    val request = ImageRequest.Builder(LocalContext.current)
        .data(imageUrl ?: R.drawable.placeholder)
        .crossfade(true)
        .build()

    AsyncImage(
        model = request,
        contentDescription = "Flower image",
        contentScale = ContentScale.Inside,
        modifier = modifier
    )
}
