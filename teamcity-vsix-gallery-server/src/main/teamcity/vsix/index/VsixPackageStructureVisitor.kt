package teamcity.vsix.index


import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildServer.serverSide.artifacts.BuildArtifact
import jetbrains.buildServer.util.FileUtil
import org.jdom.JDOMException
import teamcity.vsix.feed.VSIXMANIFEST_FILE_EXTENSION
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.util.zip.ZipInputStream

public class VsixPackageStructureVisitor(private val analysers: Collection<VsixPackageStructureAnalyser>) {

    val log = Logger.getInstance("teamcity.vsix")

    public fun visit(artifact: BuildArtifact) {
        if (analysers.isEmpty()) {
            return
        }
        // TODO: Refactor all this to a more functional style
        val vsixPackageName = artifact.getName()
        val stream = artifact.getInputStream()
        // TODO: this is not nice!
        var zipInputStream: ZipInputStream? = null
        try {
            zipInputStream = ZipInputStream(BufferedInputStream(stream))
            var zipEntry = zipInputStream?.getNextEntry()
            while (zipEntry != null) {
                if (zipEntry?.isDirectory()!!) {
                    continue
                }
                val zipEntryName = zipEntry?.getName()!!
                if (zipEntryName.endsWith(VSIXMANIFEST_FILE_EXTENSION)) {
                    log.info("Manifest file found on path $zipEntryName in VSIX package $vsixPackageName")
                    val vsixManifestContent = readVsixManifestContent(zipInputStream!!)
                    if (vsixManifestContent == null) {
                        log.warn("Failed to read .vsixmanifest file content from VSIX package $vsixPackageName")
                    } else {
                        analysers.forEach {
                            it.analyzeVsixManifest(vsixManifestContent)
                        }
                    }
                    zipInputStream?.closeEntry()
                }
            }
        } catch (ioException: IOException) {
            log.warn("Failed to read content of VSIX package " + vsixPackageName);
            log.warn(ioException.toString());
            try {
                zipInputStream?.close()

            } catch (exception: IOException) {

            }
        } finally {
            FileUtil.close(stream)
        }
    }

    private fun readVsixManifestContent(zipInputStream: ZipInputStream): VsixManifestContent? {
        try {
            val document = FileUtil.parseDocument(object: InputStream(){
                override fun close() {
                    // Do nothing
                }

                override fun read(): Int {
                    return zipInputStream.read()
                }
            }, false)
            return VsixManifestContent(document)
        } catch (exception: JDOMException) {
            log.debug(exception)
            return null
        }
    }
}