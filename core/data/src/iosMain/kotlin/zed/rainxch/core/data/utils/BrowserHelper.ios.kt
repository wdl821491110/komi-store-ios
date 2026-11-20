package zed.rainxch.core.data.utils

import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import zed.rainxch.core.domain.helpers.BrowserHelper

class IosBrowserHelper : BrowserHelper {
    override fun openUrl(url: String, onFailure: (error: String) -> Unit) {
        val nsUrl = NSURL(string = url)
        if (nsUrl == null) {
            onFailure("Invalid URL: $url")
            return
        }
        val app = UIApplication.sharedApplication
        val accepted = app.openURL(nsUrl)
        if (!accepted) onFailure("Unable to open URL: $url")
    }
}
