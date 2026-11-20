package zed.rainxch.core.data.services.noop

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import zed.rainxch.core.domain.model.account.github.GithubAsset
import zed.rainxch.core.domain.model.apk.ApkInspection
import zed.rainxch.core.domain.model.apk.ApkPackageInfo
import zed.rainxch.core.domain.model.installation.DeviceApp
import zed.rainxch.core.domain.model.installation.DhizukuAvailability
import zed.rainxch.core.domain.model.installation.RootAvailability
import zed.rainxch.core.domain.model.installation.ShizukuAvailability
import zed.rainxch.core.domain.model.installation.SystemPackageInfo
import zed.rainxch.core.domain.model.system.SystemArchitecture
import zed.rainxch.core.domain.repository.ExternalInstallerRepository
import zed.rainxch.core.domain.system.AggressiveOemDetector
import zed.rainxch.core.domain.system.ApkInspector
import zed.rainxch.core.domain.system.DownloadProgressNotifier
import zed.rainxch.core.domain.system.ExternalAppCandidate
import zed.rainxch.core.domain.system.ExternalAppScanner
import zed.rainxch.core.domain.system.InstallOutcome
import zed.rainxch.core.domain.system.Installer
import zed.rainxch.core.domain.system.InstallerInfoExtractor
import zed.rainxch.core.domain.system.InstallerStatusProvider
import zed.rainxch.core.domain.system.PackageMonitor
import zed.rainxch.core.domain.system.PendingInstallNotifier
import zed.rainxch.core.domain.system.UpdateScheduleManager
import zed.rainxch.core.domain.system.VisiblePackageEstimate

object NoOpInstaller : Installer {
    override suspend fun isSupported(extOrMime: String): Boolean = false

    override suspend fun ensurePermissionsOrThrow(extOrMime: String) = Unit

    override suspend fun install(filePath: String, extOrMime: String): InstallOutcome = InstallOutcome.DELEGATED_TO_SYSTEM

    override fun uninstall(packageName: String) = Unit

    override fun isAssetInstallable(assetName: String): Boolean = false

    override fun choosePrimaryAsset(assets: List<GithubAsset>): GithubAsset? = null

    override fun detectSystemArchitecture(): SystemArchitecture = SystemArchitecture.AARCH64

    override fun isObtainiumInstalled(): Boolean = false

    override fun openInObtainium(repoOwner: String, repoName: String, onOpenInstaller: () -> Unit) = Unit

    override fun isAppManagerInstalled(): Boolean = false

    override fun openInAppManager(filePath: String, onOpenInstaller: () -> Unit) = Unit

    override fun getApkInfoExtractor(): InstallerInfoExtractor = NoOpInstallerInfoExtractor

    override fun openApp(packageName: String): Boolean = false

    override fun openWithExternalInstaller(filePath: String) = Unit
}

object NoOpInstallerInfoExtractor : InstallerInfoExtractor {
    override suspend fun extractPackageInfo(filePath: String): ApkPackageInfo? = null
}

object NoOpPackageMonitor : PackageMonitor {
    override suspend fun isPackageInstalled(packageName: String): Boolean = false

    override suspend fun getInstalledPackageInfo(packageName: String): SystemPackageInfo? = null

    override suspend fun getAllInstalledPackageNames(): Set<String> = emptySet()

    override suspend fun getAllInstalledApps(): List<DeviceApp> = emptyList()

    override fun canEnumerateInstalledPackages(): Boolean = false
}

object NoOpApkInspector : ApkInspector {
    override suspend fun inspectFile(filePath: String): ApkInspection? = null

    override suspend fun inspectInstalled(packageName: String): ApkInspection? = null
}

object NoOpInstallerStatusProvider : InstallerStatusProvider {
    override val shizukuAvailability: StateFlow<ShizukuAvailability> = MutableStateFlow(ShizukuAvailability.UNAVAILABLE)
    override val dhizukuAvailability: StateFlow<DhizukuAvailability> = MutableStateFlow(DhizukuAvailability.UNAVAILABLE)
    override val rootAvailability: StateFlow<RootAvailability> = MutableStateFlow(RootAvailability.UNAVAILABLE)

    override fun requestShizukuPermission() = Unit

    override fun requestDhizukuPermission() = Unit

    override fun requestRootPermission() = Unit
}

object NoOpUpdateScheduleManager : UpdateScheduleManager {
    override fun reschedule(intervalHours: Long) = Unit

    override fun cancel() = Unit
}

object NoOpPendingInstallNotifier : PendingInstallNotifier {
    override fun notifyPending(packageName: String, repoOwner: String, repoName: String, appName: String, versionTag: String) = Unit

    override fun clearPending(packageName: String) = Unit
}

object NoOpDownloadProgressNotifier : DownloadProgressNotifier {
    override fun notifyProgress(
        packageName: String,
        appName: String,
        versionTag: String,
        percent: Int?,
        bytesDownloaded: Long,
        totalBytes: Long?,
        paused: Boolean,
    ) = Unit

    override fun clearProgress(packageName: String) = Unit
}

object NoOpAggressiveOemDetector : AggressiveOemDetector {
    override fun isAggressiveOem(): Boolean = false

    override fun isBatteryOptimizationIgnored(): Boolean = true

    override fun openBatteryOptimizationSettings(): Boolean = false
}

object NoOpExternalAppScanner : ExternalAppScanner {
    override suspend fun isPermissionGranted(): Boolean = false

    override suspend fun visiblePackageCountEstimate(): VisiblePackageEstimate = VisiblePackageEstimate(0, 0, false)

    override suspend fun snapshot(): List<ExternalAppCandidate> = emptyList()

    override suspend fun snapshotSingle(packageName: String): ExternalAppCandidate? = null
}

object NoOpExternalInstallerRepository : ExternalInstallerRepository {
    override suspend fun listInstallers(): List<zed.rainxch.core.domain.model.installation.ExternalInstallerApp> = emptyList()
}
