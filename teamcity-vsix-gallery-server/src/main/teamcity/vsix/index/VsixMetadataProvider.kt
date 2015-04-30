package teamcity.vsix.index

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
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.util.*

class VsixMetadataProvider() : BuildMetadataProvider {
    val VSIX_PROVIDER_ID: String = "vsix"
    val LOG = Logger.getInstance("teamcity.vsix");

    {
        LOG.info("Metadata provider initialized.")
    }

    override fun getProviderId(): String = VSIX_PROVIDER_ID

    override fun generateMedatadata(build: SBuild, store: MetadataStorageWriter) {
        LOG.info("Looking for VSIX packages in " + LogUtil.describe(build))

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

    val TEAMCITY_ARTIFACT_RELPATH: String = "teamcity.artifactPath"
    val TEAMCITY_BUILD_TYPE_ID: String = "teamcity.buildTypeId"

    public fun generateMetadataForPackage(build: SBuild, aPackage: BuildArtifact): Map<String, String> {
        val analyzer = VsixPackageStructureAnalyser(DateTime(build.getFinishDate(), DateTimeZone.UTC))
        val visitor = VsixPackageStructureVisitor(Arrays.asList(analyzer))
        visitor.visit(aPackage)

        val metadata = analyzer.getItems()
        metadata.put(TEAMCITY_ARTIFACT_RELPATH, aPackage.getRelativePath())
        metadata.put(TEAMCITY_BUILD_TYPE_ID, build.getBuildTypeId())
        LOG.info("Metadata: " + metadata)
        return metadata
    }

    // todo make this a lambda expression (Kotlin surely supports those!)
    fun visitArtifacts(artifact: BuildArtifact, packages: MutableList<BuildArtifact>) {
        if (!artifact.isDirectory()) {
            val name = artifact.getName().toLowerCase()
            if (name.endsWith(".vsix"))
                packages.add(artifact)
        }

        for (children in artifact.getChildren()) {
            visitArtifacts(children, packages)
        }
    }
}