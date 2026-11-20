package zed.rainxch.core.data.services

import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

class IosFileLocationsProvider : FileLocationsProvider {
    private val downloadsDir: String by lazy {
        val path =
            NSFileManager.defaultManager
                .URLsForDirectory(NSCachesDirectory, NSUserDomainMask)
                .firstOrNull()
                ?.let { (it as? platform.Foundation.NSURL)?.path }
                ?: NSTempFallback()
        val dir = path.trimEnd('/') + "/downloads"
        NSFileManager.defaultManager.createDirectoryAtPath(dir, withIntermediateDirectories = true, attributes = null, error = null)
        dir
    }

    private fun NSTempFallback(): String = platform.Foundation.NSTemporaryDirectory()

    override fun appDownloadsDir(): String = downloadsDir

    override fun userDownloadsDir(): String = downloadsDir

    override fun setExecutableIfNeeded(path: String) = Unit

    override fun getCacheSizeBytes(): Long {
        val attrs = NSFileManager.defaultManager.attributesOfItemAtPath(downloadsDir, error = null) ?: return 0L
        return (attrs[platform.Foundation.NSFileSize] as? Number)?.toLong() ?: 0L
    }

    override fun clearCacheFiles(): Boolean = runCatching {
        NSFileManager.defaultManager.removeItemAtPath(downloadsDir, error = null)
        NSFileManager.defaultManager.createDirectoryAtPath(downloadsDir, withIntermediateDirectories = true, attributes = null, error = null)
        true
    }.getOrDefault(false)
}
