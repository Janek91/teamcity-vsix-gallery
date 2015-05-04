package teamcity.vsix.index

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.text.StringUtil
import org.joda.time.DateTime

import java.util.LinkedHashMap


public class VsixPackageStructureAnalyser(private val finishDate: DateTime) {
    private val manifestItems = LinkedHashMap<String, String>()

    public fun getItems(): Map<String, String> {
        return manifestItems
    }

    public fun analyzeVsixManifest(manifest: VsixManifestContent) {
        addItem(ID, manifest.getId())
        addItem(VERSION, manifest.getVersion())
        addItem(TITLE, manifest.getDisplayName())
        addItem(SUMMARY, manifest.getDescription())
        addItem(LAST_UPDATED, finishDate.toString())
        addItem(PUBLISHER, manifest.getPublisher())
    }

    private fun addItem(key: String, value: String?) {
        if (!StringUtil.isEmptyOrSpaces(value)) {
            manifestItems.put(key, value)
        }
    }
}
