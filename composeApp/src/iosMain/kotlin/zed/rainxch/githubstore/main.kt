package zed.rainxch.githubstore

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.ComposeUIViewController
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.setUnhandledExceptionHook
import kotlin.native.terminateWithUnhandledException
import platform.Foundation.NSLog
import zed.rainxch.githubstore.app.di.initKoin

private var koinInitialized = false
private var crashHookInstalled = false

@OptIn(ExperimentalNativeApi::class)
private fun installCrashHook() {
    if (crashHookInstalled) return
    crashHookInstalled = true
    setUnhandledExceptionHook { throwable ->
        NSLog("KOMI_FATAL: ${throwable.stackTraceToString()}")
        terminateWithUnhandledException(throwable)
    }
}

fun MainViewController() = run {
    installCrashHook()
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
}
