package zed.rainxch.githubstore

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.ComposeUIViewController
import zed.rainxch.githubstore.app.di.initKoin

private var koinInitialized = false

fun MainViewController() =
    ComposeUIViewController {
        LaunchedEffect(Unit) {
            if (!koinInitialized) {
                initKoin()
                koinInitialized = true
            }
        }
        if (koinInitialized) {
            App()
        }
    }
