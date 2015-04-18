package teamcity.vsix.index

import jetbrains.buildServer.serverSide.metadata.BuildMetadataEntry
import jetbrains.buildServer.serverSide.metadata.MetadataStorage
import java.lang
import java.util.ArrayList

class PackagesIndex(val storage: MetadataStorage) {

    val VSIX_PROVIDER_ID: String = "vsix"

    public fun getPackageEntries() : Collection<VsixPackage> = decorateMetadata(storage.getAllEntries(VSIX_PROVIDER_ID))

    private fun decorateMetadata(allEntries: Iterator<BuildMetadataEntry>): Collection<VsixPackage> {

        // todo god help me, why isn't there a linq here???
        var result = ArrayList<VsixPackage>()
        for(entry in allEntries)
        {
            result.add(VsixPackage(entry))
        }

        return result;

    }

}

