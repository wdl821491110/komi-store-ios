package zed.rainxch.core.data.local

import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

object IosAppDataPaths {
    fun documentsDir(): String =
        NSFileManager.defaultManager
            .URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
            .firstOrNull()
            ?.let { (it as? platform.Foundation.NSURL)?.path }
            ?: error("Unable to resolve iOS Documents directory")

    fun filePath(fileName: String): String = documentsDir().trimEnd('/') + "/" + fileName

    fun ensureDir() {
        val dir = documentsDir()
        NSFileManager.defaultManager.createDirectoryAtPath(dir, withIntermediateDirectories = true, attributes = null, error = null)
    }
}
