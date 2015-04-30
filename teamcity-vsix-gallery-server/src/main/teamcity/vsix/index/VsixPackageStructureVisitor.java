package teamcity.vsix.index;

import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.serverSide.artifacts.BuildArtifact;
import jetbrains.buildServer.util.FileUtil;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teamcity.vsix.feed.FeedConstants;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class VsixPackageStructureVisitor {

    private static final Logger LOG = Logger.getInstance("teamcity.vsix");

    @NotNull
    private final Collection<VsixPackageStructureAnalyser> myAnalysers;

    public VsixPackageStructureVisitor(@NotNull Collection<VsixPackageStructureAnalyser> analysers) {
        myAnalysers = analysers;
    }

    public void visit(@NotNull BuildArtifact artifact) throws PackageLoadException {
        if(myAnalysers.isEmpty()) return;
        ZipInputStream zipInputStream = null;
        InputStream stream = null;
        final String vsixPackageName = artifact.getName();
        try {
            stream = artifact.getInputStream();
            zipInputStream = new ZipInputStream(new BufferedInputStream(stream));
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if(zipEntry.isDirectory()) continue;
                final String zipEntryName = zipEntry.getName();
                if (zipEntryName.endsWith(FeedConstants.VSIXMANIFEST_FILE_EXTENSION)) {
                    LOG.info(String.format("Manifest file found on path %s in VSIX package %s", zipEntryName, vsixPackageName));
                    final VsixManifestContent vsixManifestContent = readVsixManifestContent(zipInputStream);
                    if (vsixManifestContent == null)
                        LOG.warn("Failed to read .vsixmanifest file content from NuGet package " + vsixPackageName);
                    else {
                        for(VsixPackageStructureAnalyser analyser : myAnalysers){
                            analyser.analyzeVsixManifest(vsixManifestContent);
                        }
                    }
                    zipInputStream.closeEntry();
                }
            }
        } catch (IOException e) {
            LOG.warn("Failed to read content of NuGet package " + vsixPackageName);
            if(zipInputStream != null){
                try {
                    zipInputStream.close();
                } catch (IOException ex) {
                    //NOP
                }
            }
        } finally {
            FileUtil.close(stream);
        }
    }

    @Nullable
    private VsixManifestContent readVsixManifestContent(final ZipInputStream finalZipInputStream) throws IOException {
        try {
            final Element document = FileUtil.parseDocument(new InputStream() {
                @Override
                public int read() throws IOException {
                    return finalZipInputStream.read();
                }

                @Override
                public void close() throws IOException {
                    //do nothing, should avoid stream closing by xml parse util
                }
            }, false);
            return new VsixManifestContent(document);
        } catch (JDOMException e) {
            LOG.debug(e);
            return null;
        }
    }
}