package zed.rainxch.core.data.network

import zed.rainxch.core.domain.network.DigestVerifier

// No cryptographic digest provider is bundled for iOS in this build. Returning null means
// "no evidence", which the download pipeline treats as a non-mismatch: downloads still work,
// only optional integrity verification is skipped.
class IosDigestVerifier : DigestVerifier {
    override suspend fun verify(filePath: String, expectedDigest: String): String? = null

    override suspend fun computeSha256(filePath: String): String? = null
}
