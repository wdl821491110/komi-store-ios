package zed.rainxch.core.domain

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.localeIdentifier
import platform.UIKit.UIDevice
import zed.rainxch.core.domain.model.system.Platform

// iOS has no dedicated enum entry; MACOS is the closest Apple platform and keeps
// the isDesktop/isMobile semantics used by shared UI code intact.
actual fun getPlatform(): Platform = Platform.MACOS

actual fun getOsVersion(): String = UIDevice.currentDevice.systemVersion

actual fun getSystemLocaleTag(): String =
    NSLocale.currentLocale.localeIdentifier.takeIf { it.isNotBlank() } ?: "und"

@OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)
actual fun isValidDownloadedFile(path: String): Boolean =
    platform.Foundation.NSFileManager.defaultManager.fileExistsAtPath(path)
