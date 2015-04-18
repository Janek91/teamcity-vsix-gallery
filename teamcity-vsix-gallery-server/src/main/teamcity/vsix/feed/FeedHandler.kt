package teamcity.vsix.feed

import com.intellij.openapi.diagnostic.Logger
import teamcity.vsix.index.PackagesIndex
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class FeedHandler(val index: PackagesIndex) {
    val LOG = Logger.getInstance("teamcity.vsix");

    fun handleRequest(baseMappingPath: String, request: HttpServletRequest, response: HttpServletResponse) {
        LOG.info("Handling request");

        val entries = index.getPackageEntries()
        for(entry in entries) {
            LOG.info("Entry: " + entry.toString())
        }
    }


}