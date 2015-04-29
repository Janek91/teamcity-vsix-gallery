package teamcity.vsix.index

import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildServer.serverSide.metadata.BuildMetadataEntry

data class VsixPackage(val entry: BuildMetadataEntry) {
    val LOG = Logger.getInstance("teamcity.vsix");
    val metadata = entry.getMetadata();

    {
        LOG.info("got metadata: " + metadata)
        metadata.forEach { LOG.info("Key: " + it.getKey() + " Value: " + it.getValue()) }
    }


    public val Id: String = metadata.get("Id").orEmpty();
    public val Title: String = metadata.get("Title").orEmpty();
    public val Summary: String = metadata.get("Summary").orEmpty();
    public val Version: String = metadata.get("Version").orEmpty();
    public val AuthorName: String = metadata.get("AutorName").orEmpty();
    public val ContentPath: String = metadata.get("teamcity.artifactPath").orEmpty();

}