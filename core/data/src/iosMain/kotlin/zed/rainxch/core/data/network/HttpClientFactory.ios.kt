package zed.rainxch.core.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import zed.rainxch.core.domain.model.settings.ProxyConfig

// Darwin (NSURLSession) engine. iOS has no per-process proxy switch equivalent to
// Desktop's ProxySelector, so proxyConfig is intentionally ignored here.
actual fun createPlatformHttpClient(proxyConfig: ProxyConfig): HttpClient = HttpClient(Darwin)
