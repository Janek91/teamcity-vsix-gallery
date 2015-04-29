package teamcity.vsix.feed

import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildServer.controllers.BaseController
import jetbrains.buildServer.web.openapi.WebControllerManager
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class FeedServerController(web : WebControllerManager) : BaseController() {
    val LOG = Logger.getInstance("teamcity.vsix");

    {
        LOG.info("Initializing feed controller.")
        web.registerController("/app/vsix/v1/FeedService.svc/**", this)
    }

    override fun doHandle(request: HttpServletRequest, response: HttpServletResponse): ModelAndView? {

        LOG.info("handling request!")
        return null
    }
}