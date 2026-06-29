package com.example.utils

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipInputStream

object ZipExtractor {

    /**
     * Extracts a ZIP file provided by a SAF Uri into a specified workspace directory
     * using the DocumentFile API.
     */
    suspend fun extractZipToWorkspace(
        context: Context,
        zipUri: Uri,
        workspaceDir: File,
        onProgress: (Int) -> Unit
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            if (!workspaceDir.exists()) {
                workspaceDir.mkdirs()
            }

            val documentFile = DocumentFile.fromSingleUri(context, zipUri)
                ?: return@withContext Result.failure(Exception("Could not open DocumentFile from Uri"))

            val fileSize = documentFile.length()
            var bytesRead = 0L

            context.contentResolver.openInputStream(zipUri)?.use { inputStream ->
                ZipInputStream(inputStream).use { zis ->
                    var entry = zis.nextEntry
                    while (entry != null) {
                        val outFile = File(workspaceDir, entry.name)

                        // Protect against Zip Path Traversal Vulnerability
                        if (!outFile.canonicalPath.startsWith(workspaceDir.canonicalPath + File.separator)) {
                            throw SecurityException("Zip Path Traversal detected: ${entry.name}")
                        }

                        if (entry.isDirectory) {
                            outFile.mkdirs()
                        } else {
                            outFile.parentFile?.mkdirs()
                            FileOutputStream(outFile).use { fos ->
                                val buffer = ByteArray(4096)
                                var len: Int
                                while (zis.read(buffer).also { len = it } > 0) {
                                    fos.write(buffer, 0, len)
                                    bytesRead += len
                                    if (fileSize > 0) {
                                        val progress = ((bytesRead.toFloat() / fileSize) * 100).toInt()
                                        onProgress(progress.coerceAtMost(100))
                                    }
                                }
                            }
                        }
                        zis.closeEntry()
                        entry = zis.nextEntry
                    }
                }
            } ?: return@withContext Result.failure(Exception("Could not open InputStream"))
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
