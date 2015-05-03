package teamcity.vsix.index

import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildServer.serverSide.metadata.BuildMetadataEntry

import teamcity.vsix.index.PackageAttributes.*

data class VsixPackage(val entry: BuildMetadataEntry) {
    val LOG = Logger.getInstance("teamcity.vsix");
    val metadata = entry.getMetadata();

    {
        LOG.info("Metadata: " + metadata)
    }

    public val Id: String = metadata.get(ID).orEmpty();
    public val Title: String = metadata.get(TITLE).orEmpty();
    public val Summary: String = metadata.get(SUMMARY).orEmpty();
    public val Version: String = metadata.get(VERSION).orEmpty();
    public val Publisher: String = metadata.get(PUBLISHER).orEmpty();
    public val LastUpdated: String = metadata.get(LAST_UPDATED).orEmpty();
    public val ContentPath: String = metadata.get("teamcity.artifactPath").orEmpty();
    public val BuildTypeId: String = metadata.get("teamcity.buildTypeId").orEmpty();
    public val BuildId: String = entry.getBuildId().toString()

}