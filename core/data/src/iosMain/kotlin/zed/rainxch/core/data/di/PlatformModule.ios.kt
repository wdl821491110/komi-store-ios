package zed.rainxch.core.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.core.qualifier.named
import org.koin.dsl.module
import zed.rainxch.core.data.local.data_store.createAnnouncementsDataStore
import zed.rainxch.core.data.local.data_store.createDataStore
import zed.rainxch.core.data.local.db.AppDatabase
import zed.rainxch.core.data.local.db.initDatabase
import zed.rainxch.core.data.network.IosDigestVerifier
import zed.rainxch.core.data.services.FileLocationsProvider
import zed.rainxch.core.data.services.IosFileLocationsProvider
import zed.rainxch.core.data.services.IosLocalizationManager
import zed.rainxch.core.data.services.LocalizationManager
import zed.rainxch.core.data.services.download.IosDownloader
import zed.rainxch.core.data.services.noop.NoOpAggressiveOemDetector
import zed.rainxch.core.data.services.noop.NoOpApkInspector
import zed.rainxch.core.data.services.noop.NoOpDownloadProgressNotifier
import zed.rainxch.core.data.services.noop.NoOpExternalAppScanner
import zed.rainxch.core.data.services.noop.NoOpExternalInstallerRepository
import zed.rainxch.core.data.services.noop.NoOpInstaller
import zed.rainxch.core.data.services.noop.NoOpInstallerStatusProvider
import zed.rainxch.core.data.services.noop.NoOpPackageMonitor
import zed.rainxch.core.data.services.noop.NoOpPendingInstallNotifier
import zed.rainxch.core.data.services.noop.NoOpUpdateScheduleManager
import zed.rainxch.core.data.utils.IosAppLauncher
import zed.rainxch.core.data.utils.IosBrowserHelper
import zed.rainxch.core.data.utils.IosClipboardHelper
import zed.rainxch.core.data.utils.IosShareManager
import zed.rainxch.core.domain.diagnostics.DiagnosticSessionController
import zed.rainxch.core.domain.diagnostics.NoOpDiagnosticSessionController
import zed.rainxch.core.domain.helpers.AppLauncher
import zed.rainxch.core.domain.helpers.BrowserHelper
import zed.rainxch.core.domain.helpers.ClipboardHelper
import zed.rainxch.core.domain.helpers.ShareManager
import zed.rainxch.core.domain.network.DigestVerifier
import zed.rainxch.core.domain.network.Downloader
import zed.rainxch.core.domain.repository.ExternalInstallerRepository
import zed.rainxch.core.domain.system.AggressiveOemDetector
import zed.rainxch.core.domain.system.ApkInspector
import zed.rainxch.core.domain.system.DownloadProgressNotifier
import zed.rainxch.core.domain.system.ExternalAppScanner
import zed.rainxch.core.domain.system.Installer
import zed.rainxch.core.domain.system.InstallerStatusProvider
import zed.rainxch.core.domain.system.PackageMonitor
import zed.rainxch.core.domain.system.PendingInstallNotifier
import zed.rainxch.core.domain.system.UpdateScheduleManager

actual val corePlatformModule =
    module {
        single<Downloader> { IosDownloader(get(), get(), get()) }

        single<ExternalInstallerRepository> { NoOpExternalInstallerRepository }

        single<Installer> { NoOpInstaller }

        single<FileLocationsProvider> { IosFileLocationsProvider() }

        single<AggressiveOemDetector> { NoOpAggressiveOemDetector }

        single<PackageMonitor> { NoOpPackageMonitor }

        single<ApkInspector> { NoOpApkInspector }

        single<ExternalAppScanner> { NoOpExternalAppScanner }

        single<LocalizationManager> { IosLocalizationManager() }

        single<AppDatabase> { initDatabase() }

        single<DataStore<Preferences>> { createDataStore() }

        single<DataStore<Preferences>>(qualifier = named("announcements")) { createAnnouncementsDataStore() }

        single<eu.anifantakis.lib.ksafe.KSafe>(qualifier = named("tokens")) {
            eu.anifantakis.lib.ksafe.KSafe(fileName = "ghs_tokens")
        }

        single<eu.anifantakis.lib.ksafe.KSafe>(qualifier = named("prefs")) {
            eu.anifantakis.lib.ksafe.KSafe(fileName = "ghs_prefs")
        }

        single<eu.anifantakis.lib.ksafe.KSafe>(qualifier = named("announcements_cache")) {
            eu.anifantakis.lib.ksafe.KSafe(fileName = "ghs_announcements")
        }

        single<BrowserHelper> { IosBrowserHelper() }

        single<DigestVerifier> { IosDigestVerifier() }

        single<ClipboardHelper> { IosClipboardHelper() }

        single<AppLauncher> { IosAppLauncher() }

        single<ShareManager> { IosShareManager() }

        single<DiagnosticSessionController> { NoOpDiagnosticSessionController }

        single<InstallerStatusProvider> { NoOpInstallerStatusProvider }

        single<UpdateScheduleManager> { NoOpUpdateScheduleManager }

        single<PendingInstallNotifier> { NoOpPendingInstallNotifier }

        single<DownloadProgressNotifier> { NoOpDownloadProgressNotifier }
    }
