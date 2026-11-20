package zed.rainxch.core.data.services

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
actual fun fileExists(path: String): Boolean = NSFileManager.defaultManager.fileExistsAtPath(path)

@OptIn(ExperimentalForeignApi::class)
actual fun deleteFileAt(path: String): Boolean =
    runCatching { NSFileManager.defaultManager.removeItemAtPath(path, error = null) }.getOrDefault(false)

@OptIn(ExperimentalForeignApi::class)
actual fun readFileBytes(path: String): ByteArray {
    val data = NSFileManager.defaultManager.contentsAtPath(path) ?: return ByteArray(0)
    val size = data.length.toInt()
    val bytes = ByteArray(size)
    if (size > 0) {
        bytes.usePinned { pinned -> memcpy(pinned.addressOf(0), data.bytes, data.length) }
    }
    return bytes
}
