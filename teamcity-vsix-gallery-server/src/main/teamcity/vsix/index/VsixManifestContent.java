package teamcity.vsix.index;

import com.intellij.openapi.diagnostic.Logger;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class VsixManifestContent {

    private static final String NS = "http://schemas.microsoft.com/developer/vsx-schema/2011";
    private static final String METADATA_ELEMENT = "Metadata";
    private static final String IDENTITY = "Identity";
    private static final String ID = "Id";
    private static final String VERSION = "Version";
    private static final String PUBLISHER = "Publisher";
    private static final String DISPLAY_NAME = "DisplayName";
    private static final String DESCRIPTION = "Description";

    private static Logger LOG = Logger.getInstance("teamcity.vsix");

    private Element myContent;

    public VsixManifestContent(@NotNull Element content) {
        myContent = content;
    }

    public String getId() { return parseAttribute(getChild(myContent, METADATA_ELEMENT), IDENTITY, ID); }
    public String getVersion() { return parseAttribute(getChild(myContent, METADATA_ELEMENT) ,IDENTITY, VERSION); }
    public String getPublisher() { return parseAttribute(getChild(myContent, METADATA_ELEMENT), IDENTITY, PUBLISHER); }
    public String getDisplayName() { return parseMetadataProperty(myContent, DISPLAY_NAME); }
    public String getDescription() { return parseMetadataProperty(myContent, DESCRIPTION); }
//    public String getReleaseNotes() {
//        return parseMetadataProperty(myContent, RELEASE_NOTES_ELEMENT);
//    }
//
//    public String getDescription() {
//        return parseMetadataProperty(myContent, DESCRIPTION_ELEMENT);
//    }
//    public String getCopyright() {
//        return parseMetadataProperty(myContent, COPYRIGHT_ELEMENT);
//    }
//    public String getMinClientVersion() {
//        return parseMetadataAttribute(myContent, MIN_CLIENT_VERSION_ATTRIBUTE);
//    }
//    public String getRequireLicenseAcceptance() {
//        return parseMetadataProperty(myContent, REQUIRE_LICENSE_ACCEPTANCE_ELEMENT);
//    }
//    public String getLicenseUrl() {
//        return parseMetadataProperty(myContent, LICENSE_URL_ELEMENT);
//    }
//    public String getProjectUrl() {
//        return parseMetadataProperty(myContent, PROJECT_URL_ELEMENT);
//    }
//    public String getTags() {
//        return parseMetadataProperty(myContent, TAGS_ELEMENT);
//    }
//    public String getIconUrl() {
//        return parseMetadataProperty(myContent, ICON_URL_ELEMENT);
//    }


    @Nullable
    private String parseAttribute(@NotNull final Element root, @NotNull final String childName, @NotNull final String attribute) {
        LOG.info("parseAttribute: element: " + root + " childName: " + childName + " attribute: " + attribute);
        final Element metadata = getChild(root, childName);
        if (metadata == null){
            LOG.info("parseAttribute: child not found :(");
            return null;
        }
        String result = metadata.getAttributeValue(attribute);
        LOG.info("getAttributeValue result: " + result);
        return result;
    }

    @Nullable
    private static String parseProperty(@NotNull final Element root, @NotNull final String childName, final @NotNull String name) {
        LOG.info("parseProperty: element: " + root + " childName: " + childName + " property: " + name);
        final Element child = getChild(getChild(root, childName), name);
        return child == null ? null : child.getTextNormalize();
    }
    @Nullable
    private static String parseMetadataProperty(@NotNull final Element root, final @NotNull String name) {
        return parseProperty(root, METADATA_ELEMENT, name);
    }

    @Nullable
    private String parseMetadataAttribute(@NotNull final Element root, @NotNull final String attribute) {
        return parseAttribute(root, METADATA_ELEMENT, attribute);
    }

    @Nullable
    private static Element getChild(@Nullable final Element root, final String childName) {
        if (root == null) return null;
        Element child = root.getChild(childName);
        if (child != null) return child;
        return root.getChild(childName, root.getNamespace(NS));
    }

    @NotNull
    private static List<Element> getChildren(@Nullable final Element root, final String child) {
        if (root == null) return Collections.emptyList();
        List<Element> result = new ArrayList<Element>();
        for (List list : Arrays.asList(root.getChildren(child), root.getChildren(child, root.getNamespace(NS)))) {
            for (Object o : list) {
                result.add((Element)o);
            }
        }
        return result;
    }
}
