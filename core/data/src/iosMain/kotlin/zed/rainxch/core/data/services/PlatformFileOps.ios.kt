package zed.rainxch.core.data.services

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSFileManager

@OptIn(ExperimentalForeignApi::class)
actual fun fileExists(path: String): Boolean = NSFileManager.defaultManager.fileExistsAtPath(path)

@OptIn(ExperimentalForeignApi::class)
actual fun deleteFileAt(path: String): Boolean =
    runCatching { NSFileManager.defaultManager.removeItemAtPath(path, error = null) }.getOrDefault(false)
