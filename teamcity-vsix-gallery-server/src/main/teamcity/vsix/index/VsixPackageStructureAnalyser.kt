package teamcity.vsix.index

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.text.StringUtil
import org.joda.time.DateTime

import java.util.LinkedHashMap


public class VsixPackageStructureAnalyser(private val finishDate: DateTime) {
    private val manifestItems = LinkedHashMap<String, String>()

    public fun getItems(): MutableMap<String, String> {
        return manifestItems
    }

    public fun analyzeVsixManifest(manifest: VsixManifestContent) {
        addItem(ID, manifest.id)
        addItem(VERSION, manifest.version)
        addItem(TITLE, manifest.displayName)
        addItem(SUMMARY, manifest.description)
        addItem(LAST_UPDATED, finishDate.toString())
        addItem(PUBLISHER, manifest.publisher)
    }

    private fun addItem(key: String, value: String) {
        if (!StringUtil.isEmptyOrSpaces(value)) {
            manifestItems.put(key, value)
        }
    }
}
