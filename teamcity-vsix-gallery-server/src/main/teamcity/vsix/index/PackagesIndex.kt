package teamcity.vsix.index

import com.google.common.collect.Lists
import jetbrains.buildServer.serverSide.metadata.BuildMetadataEntry
import jetbrains.buildServer.serverSide.metadata.MetadataStorage
import java.lang
import java.util.ArrayList

class PackagesIndex(val storage: MetadataStorage) {

    val VSIX_PROVIDER_ID: String = "vsix"

    public fun getPackageEntries() : Collection<VsixPackage> = Lists.newArrayList(storage.getAllEntries(VSIX_PROVIDER_ID)).map { VsixPackage(it) }

}

