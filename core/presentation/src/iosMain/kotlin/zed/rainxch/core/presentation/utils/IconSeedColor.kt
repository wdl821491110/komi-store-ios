package zed.rainxch.core.presentation.utils

import androidx.compose.ui.graphics.ImageBitmap
import coil3.PlatformContext

// iOS has no installed-app icons; the seed falls back to no color. Image-based seeding
// is intentionally skipped to avoid platform-specific bitmap conversions here.
internal actual suspend fun loadIconBitmap(
    context: PlatformContext,
    packageName: String?,
    imageUrl: String?,
): ImageBitmap? = null
