package teamcity.vsix.index

import jetbrains.buildServer.serverSide.metadata.BuildMetadataEntry

data class VsixPackage(val entry: BuildMetadataEntry) {
    val metadata = entry.getMetadata();

    public val ID: String? = metadata.get("Id");
}