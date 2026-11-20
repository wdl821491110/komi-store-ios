package zed.rainxch.core.data.services

import java.io.File

actual fun fileExists(path: String): Boolean = File(path).exists()

actual fun deleteFileAt(path: String): Boolean = File(path).delete()
