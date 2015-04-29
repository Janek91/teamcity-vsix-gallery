package teamcity.vsix.index;

import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static teamcity.vsix.index.PackageAttributes.*;

public class VsixPackageStructureAnalyser {
    private final Map<String, String> myItems = new LinkedHashMap<String, String>();
    private final Date finishDate;

    @NotNull
    public Map<String, String> getItems() {
        return myItems;
    }

    public VsixPackageStructureAnalyser(Date finishDate) {
        this.finishDate = finishDate;
    }

    public void analyseEntry(@NotNull String entryName) {

    }

    public void analyzeVsixManifest(@NotNull VsixManifestContent manifest) {
        addItem(ID, manifest.getId());
        addItem(TITLE, manifest.getDisplayName());
        addItem(SUMMARY, manifest.getDescription());
        addItem(LAST_UPDATED, finishDate.toString());
    }

    private void addItem(@NotNull final String key, @Nullable final String value) {
        if (!StringUtil.isEmptyOrSpaces(value)) {
            myItems.put(key, value);
        }
    }
}
