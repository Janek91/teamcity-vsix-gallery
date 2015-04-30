package teamcity.vsix.index;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;

import java.util.LinkedHashMap;
import java.util.Map;

import static teamcity.vsix.index.PackageAttributes.*;

public class VsixPackageStructureAnalyser {
    private final Map<String, String> manifestItems = new LinkedHashMap<String, String>();
    private final DateTime finishDate;

    static Logger LOG = Logger.getInstance("teamcity.vsix");

    @NotNull
    public Map<String, String> getItems() {
        return manifestItems;
    }

    public VsixPackageStructureAnalyser(DateTime finishDate) {
        this.finishDate = finishDate;
    }

    public void analyzeVsixManifest(@NotNull VsixManifestContent manifest) {
        addItem(ID, manifest.getId());
        addItem(VERSION, manifest.getVersion());
        addItem(TITLE, manifest.getDisplayName());
        addItem(SUMMARY, manifest.getDescription());
        addItem(LAST_UPDATED, finishDate.toString());
        addItem(PUBLISHER, manifest.getPublisher());
    }

    private void addItem(@NotNull final String key, @Nullable final String value) {
        if (!StringUtil.isEmptyOrSpaces(value)) {
            manifestItems.put(key, value);
        }
    }
}
