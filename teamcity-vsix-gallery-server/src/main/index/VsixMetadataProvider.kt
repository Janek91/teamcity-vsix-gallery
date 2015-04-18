package index

import com.google.common.collect.Lists
import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildServer.log.Loggers
import jetbrains.buildServer.serverSide.SBuild
import jetbrains.buildServer.serverSide.artifacts.BuildArtifact
import jetbrains.buildServer.serverSide.artifacts.BuildArtifactsViewMode
import jetbrains.buildServer.serverSide.dependency.Dependency
import jetbrains.buildServer.serverSide.impl.LogUtil
import jetbrains.buildServer.serverSide.metadata.BuildMetadataProvider
import jetbrains.buildServer.serverSide.metadata.MetadataStorageWriter
import jetbrains.buildServer.util.StringUtil
import java.util.*

class VsixMetadataProvider() : BuildMetadataProvider {
    val VSIX_EXTENSION: String = ".vsix"
    val LOG = Loggers.SERVER;
    {
        LOG.info("VsixGallery: Metadata provider initialized")
    }

    override fun getProviderId(): String = VSIX_EXTENSION.substring(1)

    override fun generateMedatadata(build: SBuild, store: MetadataStorageWriter) {
        LOG.debug("Looking for VSIX packages in " + LogUtil.describe(build))

        val packages = ArrayList<BuildArtifact>()
        visitArtifacts(build.getArtifacts(BuildArtifactsViewMode.VIEW_ALL).getRootArtifact(), packages)

        for (aPackage in packages) {
            LOG.info("Indexing VSIX package from artifact " + aPackage.getRelativePath() + " of build " + LogUtil.describe(build))
            try {
                val metadata = generateMetadataForPackage(build, aPackage)
                //myReset.resetCache()
                store.addParameters(metadata.get("Id"), metadata)
            } catch (e: PackageLoadException) {
                LOG.warn("Failed to read VSIX package: " + aPackage)
            }

        }
    }

    // todo make this a lambda expression (Kotlin surely supports those!)
    fun visitArtifacts(artifact: BuildArtifact, packages: MutableList<BuildArtifact>) {
        if (!artifact.isDirectory()) {
            val name = artifact.getName().toLowerCase()
            if (name.endsWith(VSIX_EXTENSION))
                packages.add(artifact)
            return
        }

        for (children in artifact.getChildren()) {
            visitArtifacts(children, packages)
        }
    }

    //throws(javaClass<PackageLoadException>())
    public fun generateMetadataForPackage(build: SBuild, aPackage: BuildArtifact): Map<String, String> {
        val metadata = LinkedHashMap<String, String>()
        metadata.put("Id", aPackage.getName())
        return metadata
    }
}