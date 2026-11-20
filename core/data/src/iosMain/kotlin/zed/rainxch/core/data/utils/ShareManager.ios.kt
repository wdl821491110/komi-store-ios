package zed.rainxch.core.data.utils

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.dataUsingEncoding
import platform.Foundation.NSUTF8StringEncoding
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import zed.rainxch.core.domain.helpers.ShareManager

@OptIn(ExperimentalForeignApi::class)
class IosShareManager : ShareManager {
    override fun shareText(text: String) = present(items = listOf(text))

    override fun shareFile(fileName: String, content: String, mimeType: String) {
        val data = (content as NSString).dataUsingEncoding(NSUTF8StringEncoding) ?: return present(items = listOf(content))
        val url = writeTempFile(fileName, data) ?: return present(items = listOf(content))
        present(items = listOf(url))
    }

    override fun pickFile(mimeType: String, onResult: (String?) -> Unit) {
        onResult(null)
    }

    private fun present(items: List<Any>) {
        dispatch_async(dispatch_get_main_queue()) {
            val root = UIApplication.sharedApplication.keyWindow?.rootViewController ?: return@dispatch_async
            val activity = UIActivityViewController(activityItems = items, applicationActivities = null)
            root.presentViewController(activity, animated = true, completion = null)
        }
    }

    private fun writeTempFile(fileName: String, data: NSData): NSURL? {
        val dir = platform.Foundation.NSTemporaryDirectory()
        val path = dir.trimEnd('/') + "/" + fileName
        return if (platform.Foundation.NSFileManager.defaultManager.createFileAtPath(path, contents = data, attributes = null)) {
            NSURL.fileURLWithPath(path)
        } else {
            null
        }
    }
}
