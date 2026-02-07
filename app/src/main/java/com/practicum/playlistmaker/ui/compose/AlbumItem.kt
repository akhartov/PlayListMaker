package com.practicum.playlistmaker.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.theme.YsTheme

@Composable
fun AlbumItem(
    coverUrl: String = "",
    albumName: String = "album name",
    albumTracksCount: String = "0 tracks",
    modifier: Modifier
) {
    val placeholder = rememberVectorPainter(
        image = ImageVector.vectorResource(id = R.drawable.track_placeholder)
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {

        AsyncImage(
            model = coverUrl,
            contentDescription = null,
            placeholder = placeholder,
            error = placeholder,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .defaultMinSize(minWidth = 160.dp, minHeight = 160.dp)
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.big_image_radius)))
        )
        Text(
            text = albumName,
            style = MaterialTheme.typography.displaySmall,
            maxLines = 1,
            color = MaterialTheme.colorScheme.onBackground,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            text = albumTracksCount,
            style = MaterialTheme.typography.displaySmall,
            maxLines = 1,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LightView() {
    YsTheme {
        AlbumItem(
            coverUrl = "https://a.d-cd.net/jOkvtrqzWH2iV1F2M0dOggoBzCU-960.jpg",
            albumName = "my album",
            albumTracksCount = "15 tracks",
            modifier = Modifier
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun NightView() {
    YsTheme {
        AlbumItem(
            coverUrl = "https://a.d-cd.net/jOkvtrqzWH2iV1F2M0dOggoBzCU-960.jpg",
            albumName = "my album",
            albumTracksCount = "15 tracks",
            modifier = Modifier
        )
    }
}