package com.practicum.playlistmaker.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.practicum.playlistmaker.ui.NormalTrackText
import com.practicum.playlistmaker.ui.SmallestTrackText
import com.practicum.playlistmaker.ui.theme.YsTheme
import com.practicum.playlistmaker.ui.theme.getInactiveColor


@Composable
fun TrackItem(
    artworkUrl: String = "",
    trackName: String = "track name",
    artistName: String = "artist name",
    trackTime: String = "00:00",
    modifier: Modifier
) {
    val placeholder =
        rememberVectorPainter(ImageVector.vectorResource(id = R.drawable.track_placeholder))
    val forwardIcon = ImageVector.vectorResource(id = R.drawable.ic_arrow_forward)
    val ellipseIcon = ImageVector.vectorResource(id = R.drawable.ic_track_ellipse)


    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(61.dp)
            .padding(vertical = dimensionResource(id = R.dimen.track_vertical_border)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AsyncImage(
            model = artworkUrl,
            contentDescription = null,
            placeholder = placeholder,
            error = placeholder,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .defaultMinSize(minWidth = dimensionResource(id = R.dimen.track_image_size))
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = trackName,
                style = NormalTrackText,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onBackground,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.wrapContentSize()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = artistName,
                    style = SmallestTrackText,
                    maxLines = 1,
                    color = getInactiveColor(darkTheme = isSystemInDarkTheme()),
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f, fill = false)  // Занимает всё оставшееся место, но не больше
                        .wrapContentWidth()                 // Не расширяется сверх необходимого
                )

                Image(
                    imageVector = ellipseIcon,
                    contentDescription = null,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.track_dot_border))
                )

                Text(
                    text = trackTime,
                    style = SmallestTrackText,
                    maxLines = 1,
                    color = getInactiveColor(darkTheme = isSystemInDarkTheme()),
                    modifier = Modifier.wrapContentSize()
                )
            }
        }

        Image(
            imageVector = forwardIcon,
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterVertically),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LightTrackView() {
    YsTheme {
        TrackItem(
            artworkUrl = "https://a.d-cd.net/jOkvtrqzWH2iV1F2M0dOggoBzCU-960.jpg",
            modifier = Modifier
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun NightTrackView() {
    YsTheme {
        TrackItem(modifier = Modifier)
    }
}

@Preview(showBackground = true)
@Composable
fun TrackViewLongTrackName() {
    YsTheme {
        TrackItem(
            trackName = "Here Comes The Sun (Remastered 2020) extra symbols",
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TrackViewLongArtistName() {
    YsTheme {
        TrackItem(
            artistName = "Artist name of song Here Comes The Sun (Remastered 2020) extra symbols",
            modifier = Modifier
        )
    }
}
