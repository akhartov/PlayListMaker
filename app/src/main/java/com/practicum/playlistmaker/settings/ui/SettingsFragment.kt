package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_NO
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.theme.YsTheme
import com.practicum.playlistmaker.ui.theme.getSettingsIconColor
import com.practicum.playlistmaker.ui.theme.getSwitchColors
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.Boolean

class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // Автоматически управляет жизненным циклом композиции
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                YsTheme {
                    SettingsScreen(
                        viewModel.getNightModeLiveData().observeAsState(),
                        onThemeSwitch = { isNight -> viewModel.setNightMode(isNight) },
                        onShareApp = { viewModel.shareApp() },
                        onWriteSupport = { viewModel.askSupport() },
                        onUserAgreement = { viewModel.openAgreement() }
                    )
                }
            }
        }
    }

    @Preview(
        uiMode = UI_MODE_NIGHT_YES,
        showBackground = true,
        name = "Dark Mode"
    )
    @Composable
    fun NightSettingsState() {
        YsTheme {
            val nightModeState = remember { mutableStateOf(false) }
            SettingsScreen(
                nightModeState,
                onThemeSwitch = { },
                onShareApp = { },
                onWriteSupport = { },
                onUserAgreement = { }
            )
        }
    }

    @Preview(
        uiMode = UI_MODE_NIGHT_NO,
        showBackground = true,
        name = "Light Mode"
    )
    @Composable
    fun LightSettingsState() {
        YsTheme {
            val nightModeState = remember { mutableStateOf(false) }
            SettingsScreen(
                nightModeState,
                onThemeSwitch = { },
                onShareApp = { },
                onWriteSupport = { },
                onUserAgreement = { }
            )
        }
    }

    @Composable
    fun SettingsScreen(
        nightModeState: State<Boolean?>,
        onThemeSwitch: (Boolean) -> Unit,
        onShareApp: () -> Unit,
        onWriteSupport: () -> Unit,
        onUserAgreement: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SwitcherItem(nightModeState, onThemeSwitch)
            SettingItem(R.string.share_app, R.drawable.ic_share_app, onShareApp)
            SettingItem(R.string.write_support, R.drawable.ic_write_support, onWriteSupport)
            SettingItem(R.string.user_agreement, R.drawable.ic_arrow_forward, onUserAgreement)
        }
    }

    @Composable
    private fun SwitcherItem(nightModeState: State<Boolean?>, onThemeSwitch: (Boolean) -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.night_theme),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = nightModeState.value ?: false,
                onCheckedChange = onThemeSwitch,
                colors = getSwitchColors(darkTheme = isSystemInDarkTheme()),
                modifier = Modifier.align(Alignment.CenterVertically),
            )
        }
    }

    @Composable
    private fun SettingItem(
        @StringRes textId: Int,
        @DrawableRes imageId: Int,
        onClick: () -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .clickable(onClick = onClick)
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(textId),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = ImageVector.vectorResource(imageId),
                tint = getSettingsIconColor(darkTheme = isSystemInDarkTheme()),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}