package zed.rainxch.core.presentation.utils

fun formatFileSize(
    bytes: Long,
    decimals: Int = 1,
): String {
    if (bytes < 1_024L) return "$bytes B"

    val units = listOf("KB", "MB", "GB", "TB", "PB")
    var size = bytes.toDouble() / 1_024.0
    var index = 0
    while (size >= 1_024.0 && index < units.lastIndex) {
        size /= 1_024.0
        index++
    }

    var factor = 1.0
    repeat(decimals) { factor *= 10.0 }
    val rounded = (size * factor + 0.5).toLong().toDouble() / factor
    val text =
        if (decimals == 0) {
            rounded.toLong().toString()
        } else {
            val raw = rounded.toString()
            val dot = raw.indexOf('.')
            if (dot < 0) {
                raw
            } else {
                val needed = (dot + 1 + decimals).coerceAtMost(raw.length)
                raw.substring(0, needed).trimEnd('0').trimEnd('.')
            }
        }
    return "$text ${units[index]}"
}
