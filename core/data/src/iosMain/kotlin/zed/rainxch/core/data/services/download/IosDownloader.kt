package zed.rainxch.core.data.services.download

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import platform.Foundation.NSFileManager
import zed.rainxch.core.data.data_source.TokenStore
import zed.rainxch.core.data.services.FileLocationsProvider
import zed.rainxch.core.domain.model.installation.DownloadProgress
import zed.rainxch.core.domain.network.AssetIdentity
import zed.rainxch.core.domain.network.DigestVerifier
import zed.rainxch.core.domain.network.Downloader

@OptIn(ExperimentalForeignApi::class)
class IosDownloader(
    private val files: FileLocationsProvider,
    private val tokenStore: TokenStore,
    private val digestVerifier: DigestVerifier,
) : Downloader {
    private val client = HttpClient(Darwin)
    private val cancelFlags = mutableSetOf<String>()
    private val lock = Mutex()

    override fun download(
        url: String,
        suggestedFileName: String?,
        bypassMirror: Boolean,
        identity: AssetIdentity?,
    ): Flow<DownloadProgress> = flow {
        val fileName = fileNameOf(url, suggestedFileName)
        val path = saveToFile(url, fileName)
        val size = fileSize(path)
        emit(DownloadProgress(bytesDownloaded = size, totalBytes = size, percent = 100))
    }.flowOn(Dispatchers.Default)

    override suspend fun saveToFile(url: String, suggestedFileName: String?): String {
        val fileName = fileNameOf(url, suggestedFileName)
        val dir = files.appDownloadsDir().trimEnd('/')
        NSFileManager.defaultManager.createDirectoryAtPath(dir, withIntermediateDirectories = true, attributes = null, error = null)
        val path = "$dir/$fileName"
        val token = runCatching { tokenStore.blockingCurrentToken()?.accessToken?.trim() }.getOrNull()

        val response: HttpResponse = client.get(url) {
            if (!token.isNullOrBlank()) header(HttpHeaders.Authorization, "Bearer $token")
        }
        if (!response.status.isSuccess()) error("Download failed: HTTP ${response.status.value}")

        val bytes: ByteArray = response.body()
        withContext(Dispatchers.Default) {
            val nsData = platform.Foundation.NSData(bytes = bytes, length = bytes.size.toULong())
            NSFileManager.defaultManager.createFileAtPath(path, contents = nsData, attributes = null)
        }
        lock.withLock { cancelFlags.remove(fileName) }
        return path
    }

    override suspend fun getDownloadedFilePath(fileName: String): String? {
        val path = files.appDownloadsDir().trimEnd('/') + "/" + fileName
        return if (NSFileManager.defaultManager.fileExistsAtPath(path)) path else null
    }

    override suspend fun cancelDownload(fileName: String): Boolean = lock.withLock { cancelFlags.add(fileName) }

    override suspend fun discardPartial(fileName: String): Boolean = runCatching {
        val path = files.appDownloadsDir().trimEnd('/') + "/" + fileName
        NSFileManager.defaultManager.removeItemAtPath(path, error = null)
        true
    }.getOrDefault(false)

    override suspend fun reclaimOrphanedPartials(claimedNames: Set<String>): Int = 0

    private fun fileNameOf(url: String, suggested: String?): String =
        suggested?.takeIf { it.isNotBlank() } ?: url.substringAfterLast('/').substringBefore('?').ifBlank { "download.bin" }

    private fun fileSize(path: String): Long {
        val attrs = NSFileManager.defaultManager.attributesOfItemAtPath(path, error = null) ?: return 0L
        return (attrs[platform.Foundation.NSFileSize] as? Number)?.toLong() ?: 0L
    }
}
