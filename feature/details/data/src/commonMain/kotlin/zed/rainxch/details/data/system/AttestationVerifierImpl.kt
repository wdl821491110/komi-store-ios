package zed.rainxch.details.data.system

import kotlinx.coroutines.CancellationException
import org.kotlincrypto.hash.sha2.SHA256
import zed.rainxch.core.data.services.readFileBytes
import zed.rainxch.core.domain.logging.KomiStoreLogger
import zed.rainxch.details.domain.repository.DetailsRepository
import zed.rainxch.details.domain.system.AttestationVerifier
import zed.rainxch.details.domain.system.VerificationResult

class AttestationVerifierImpl(
    private val detailsRepository: DetailsRepository,
    private val logger: KomiStoreLogger,
) : AttestationVerifier {
    override suspend fun verify(
        owner: String,
        repoName: String,
        filePath: String,
    ): VerificationResult =
        try {
            val digest = computeSha256(filePath)
            val hasAttestation = detailsRepository.checkAttestations(owner, repoName, digest)
            if (hasAttestation) VerificationResult.Verified else VerificationResult.Unverified
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            logger.debug("Attestation check error: ${e.message}")
            VerificationResult.Error(e.message ?: "Unknown error")
        }

    private fun computeSha256(filePath: String): String {
        val digest = SHA256().digest(readFileBytes(filePath))
        return buildString(digest.size * 2) {
            for (byte in digest) {
                val v = byte.toInt() and 0xff
                if (v < 0x10) append('0')
                append(v.toString(16))
            }
        }
    }
}
