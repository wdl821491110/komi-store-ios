package zed.rainxch.core.data.services

import platform.Foundation.NSBundle
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

class IosLocalizationManager : LocalizationManager {
    override fun getCurrentLanguageCode(): String =
        NSBundle.mainBundle.preferredLocalizations.firstOrNull() as? String
            ?: NSLocale.currentLocale.languageCode
            ?: "en"

    override fun getPrimaryLanguageCode(): String = NSLocale.currentLocale.languageCode ?: "en"

    override fun setActiveLanguageTag(tag: String?) = Unit
}
