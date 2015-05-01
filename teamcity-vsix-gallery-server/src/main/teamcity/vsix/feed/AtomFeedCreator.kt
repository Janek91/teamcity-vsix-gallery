package teamcity.vsix.feed

import com.intellij.openapi.diagnostic.Logger
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import teamcity.vsix.index.PackagesIndex
import teamcity.vsix.index.VsixPackage
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import java.util.ArrayList
import com.mycila.xmltool.XMLDoc
import jetbrains.buildServer.serverSide.ProjectManager
import jetbrains.buildServer.util.Util
import jetbrains.buildServer.web.util.WebUtil

class AtomFeedCreator(val index: PackagesIndex, val projects: ProjectManager) {
    val LOG = Logger.getInstance("teamcity.vsix");
    val gallery_id = "uuid:38ba4411-63e2-425c-8798-7dace9b99c39;id=1"

    fun handleRequest(request: HttpServletRequest, response: HttpServletResponse) {
        val entries = index.getPackageEntries()

        LOG.info("Got entries: " + entries)

        val xml = createFeed2(entries);
        response.setContentType("application/xml");
        response.getOutputStream().write(xml.toByteArray())
    }

    fun createFeed2(packages: Collection<VsixPackage>): String {
        // todo replace with Util.doUnderContextClassLoader()

        val current = Thread.currentThread().getContextClassLoader()
        Thread.currentThread().setContextClassLoader(javaClass<XMLDoc>().getClassLoader())
        try {
            val tag = XMLDoc.newDocument(true)
                    .addRoot("feed")
                    .addTag("title").addText("VSIX Gallery")
                    .gotoRoot().addTag("id").setText(gallery_id)
                    .gotoRoot().addTag("updated").addText(DateTime.now(DateTimeZone.UTC).toString())

            packages.forEach {
                val entry = tag.addTag("entry")
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

            return tag.toString().replace("<feed>", "<feed xmlns=\"http://www.w3.org/2005/Atom\">") // I don't have a better idea...
        } finally {
            Thread.currentThread().setContextClassLoader(current)
        }
    }

    private fun getArtifactPath(vsix: VsixPackage): String {
        val externalBuildId = projects.findBuildTypeById(vsix.BuildTypeId).getExternalId()
        return WebUtil.GUEST_AUTH_PREFIX + "repository/download/" + externalBuildId + "/" + vsix.BuildId + ":id/" + vsix.ContentPath
    }
}