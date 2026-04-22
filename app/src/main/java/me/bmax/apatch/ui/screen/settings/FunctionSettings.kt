package me.bmax.apatch.ui.screen.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.bmax.apatch.APApplication
import me.bmax.apatch.R
import me.bmax.apatch.ui.component.ExpressiveCard
import me.bmax.apatch.ui.component.ToggleSettingCard
import me.bmax.apatch.util.setHideServiceEnabled

@Composable
fun FunctionSettingsContent(
    kPatchReady: Boolean,
    aPatchReady: Boolean,
    isHideServiceEnabled: Boolean,
    onHideServiceChange: (Boolean) -> Unit,
    isKernelSpoofEnabled: Boolean,
    onKernelSpoofChange: (Boolean) -> Unit,
    kernelSpoofVersion: String,
    onKernelSpoofVersionChange: (String) -> Unit,
    kernelSpoofBuildTime: String,
    onKernelSpoofBuildTimeChange: (String) -> Unit,
    onKernelSpoofSave: () -> Unit,
    onKernelSpoofRestore: () -> Unit,
    snackBarHost: SnackbarHostState,
    onNavigateToUmountConfig: () -> Unit = {},
    flat: Boolean = false,
) {
    val context = LocalContext.current
    val prefs = APApplication.sharedPreferences

    val hideServiceTitle = stringResource(id = R.string.settings_hide_service)
    val hideServiceSummary = stringResource(id = R.string.settings_hide_service_summary)
    val umountServiceTitle = stringResource(id = R.string.settings_umount_service)
    val umountServiceSummary = stringResource(id = R.string.settings_umount_service_summary)

    if (kPatchReady && aPatchReady) {
        ToggleSettingCard(
            flat = flat,
            title = hideServiceTitle,
            description = hideServiceSummary,
            checked = isHideServiceEnabled,
            onCheckedChange = {
                setHideServiceEnabled(it)
                onHideServiceChange(it)
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        ExpressiveCard(flat = flat, onClick = { onNavigateToUmountConfig() }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = umountServiceTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = umountServiceSummary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        val kernelSpoofTitle = stringResource(id = R.string.settings_kernel_spoof)
        val kernelSpoofSummary = stringResource(id = R.string.settings_kernel_spoof_summary)
        val versionLabel = stringResource(id = R.string.settings_kernel_spoof_version)
        val buildTimeLabel = stringResource(id = R.string.settings_kernel_spoof_build_time)

        ExpressiveCard(flat = flat) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = kernelSpoofTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = kernelSpoofSummary,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Switch(
                        checked = isKernelSpoofEnabled,
                        onCheckedChange = onKernelSpoofChange,
                    )
                }

                AnimatedVisibility(visible = isKernelSpoofEnabled) {
                    Column(modifier = Modifier.padding(top = 12.dp)) {
                        OutlinedTextField(
                            value = kernelSpoofVersion,
                            onValueChange = onKernelSpoofVersionChange,
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(versionLabel) },
                            singleLine = true,
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = kernelSpoofBuildTime,
                            onValueChange = onKernelSpoofBuildTimeChange,
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(buildTimeLabel) },
                            singleLine = true,
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Button(
                                onClick = onKernelSpoofSave,
                                modifier = Modifier.weight(1f),
                            ) {
                                Text(stringResource(R.string.save))
                            }
                            OutlinedButton(
                                onClick = onKernelSpoofRestore,
                                modifier = Modifier.weight(1f),
                            ) {
                                Text(stringResource(R.string.settings_kernel_spoof_restore))
                            }
                        }
                    }
                }
            }
        }
    }
}
