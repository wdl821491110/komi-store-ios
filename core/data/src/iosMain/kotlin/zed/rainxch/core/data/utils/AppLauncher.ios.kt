package zed.rainxch.core.data.utils

import zed.rainxch.core.domain.helpers.AppLauncher
import zed.rainxch.core.domain.model.installation.InstalledApp

class IosAppLauncher : AppLauncher {
    override suspend fun launchApp(installedApp: InstalledApp): Result<Unit> = Result.failure(UnsupportedOperationException("iOS cannot launch other apps"))

    override suspend fun canLaunchApp(installedApp: InstalledApp): Boolean = false
}
