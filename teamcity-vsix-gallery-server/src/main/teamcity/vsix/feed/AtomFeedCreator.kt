package teamcity.vsix.feed

import com.intellij.openapi.diagnostic.Logger
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import teamcity.vsix.index.VsixPackage
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import com.mycila.xmltool.XMLDoc
import jetbrains.buildServer.serverSide.ProjectManager
import jetbrains.buildServer.serverSide.metadata.MetadataStorage
import jetbrains.buildServer.web.util.WebUtil

class AtomFeedCreator(val storage: MetadataStorage, val projects: ProjectManager) {
    val LOG = Logger.getInstance("teamcity.vsix");
    val gallery_id = "uuid:38ba4411-63e2-425c-8798-7dace9b99c39;id=1"

    fun handleRequest(request: HttpServletRequest, response: HttpServletResponse) {
        val entries = storage.getAllEntries(VSIX_PROVIDER_ID).asSequence().map { VsixPackage(it) }

        LOG.debug("Got entries: " + entries)

        // gets the distinct last updated entries
        val latestEntries  = entries.groupBy { it.Id }.map { it.value.sortedByDescending { it.LastUpdated }.first() }

        LOG.debug("Latest: " + latestEntries)

        val xml = createFeed(latestEntries);
        response.contentType = "application/xml";
        response.outputStream.write(xml.toByteArray())
    }

    fun createFeed(packages: Collection<VsixPackage>): String {
        // todo replace with Util.doUnderContextClassLoader()

        val current = Thread.currentThread().contextClassLoader
        Thread.currentThread().contextClassLoader = XMLDoc::class.java.classLoader;
        try {
            val tag = XMLDoc.newDocument(true)
                    .addRoot("feed")
                    .addTag("title").addText("VSIX Gallery")
                    .gotoRoot().addTag("id").setText(gallery_id)
                    .gotoRoot().addTag("updated").addText(DateTime.now(DateTimeZone.UTC).toString())

            packages.forEach {
                val entry = tag.gotoRoot().addTag("entry")
                entry.addTag("id").addText(it.Id)
                entry.addTag("title").addAttribute("type", "text").addText(it.Title)
                entry.addTag("summary").addAttribute("type", "text").addText(it.Summary)
                entry.addTag("published").addText(it.LastUpdated)
                entry.addTag("updated").addText(it.LastUpdated)
                entry.addTag("author").addTag("name").addText(it.Publisher)
                entry.gotoParent().addTag("content").addAttribute("type", "application/octet-stream").addAttribute("src", getArtifactPath(it))
                val vsix = entry.gotoParent().addTag("Vsix")
                vsix.addTag("Id").addText(it.Id)
                vsix.addTag("Version").addText(it.Version)
            }

            return tag.toString().replace("<feed>", "<feed xmlns=\"http://www.w3.org/2005/Atom\">")
                                 .replace("<Vsix>", "<Vsix xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://schemas.microsoft.com/developer/vsx-syndication-schema/2010\">")
                                 // I don't have a better idea...
        } finally {
            Thread.currentThread().contextClassLoader = current
        }
    }

    private fun getArtifactPath(vsix: VsixPackage): String {
        val externalBuildId = projects.findBuildTypeById(vsix.BuildTypeId)!!.externalId
        return "${WebUtil.GUEST_AUTH_PREFIX}repository/download/$externalBuildId/${vsix.BuildId}:id/${vsix.ContentPath}"
    }
}