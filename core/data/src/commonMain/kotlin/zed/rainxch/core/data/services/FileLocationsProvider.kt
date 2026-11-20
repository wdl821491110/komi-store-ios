package zed.rainxch.core.data.services

interface FileLocationsProvider {
    fun appDownloadsDir(): String

    fun userDownloadsDir(): String

    fun setExecutableIfNeeded(path: String)

    fun getCacheSizeBytes(): Long

    fun clearCacheFiles(): Boolean
}

expect fun fileExists(path: String): Boolean

expect fun deleteFileAt(path: String): Boolean

expect fun readFileBytes(path: String): ByteArray
