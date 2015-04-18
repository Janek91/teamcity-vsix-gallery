package teamcity.vsix.feed

import jetbrains.buildServer.controllers.BaseController
import jetbrains.buildServer.web.openapi.WebControllerManager
import jetbrains.buildServer.web.util.WebUtil
import org.springframework.web.servlet.ModelAndView
import java.util.Date
import javax.servlet.http.*

class FeedController(web: WebControllerManager, val handler: FeedHandler) : BaseController() {

    override fun doHandle(request: HttpServletRequest, response: HttpServletResponse): ModelAndView? {
        var requestPath = WebUtil.getPathWithoutAuthenticationType(request)
        if (!requestPath.startsWith("/")) requestPath = "/" + requestPath

        handler.handleRequest("/app/vsix/v1/FeedService.svc", request, response)
        return null
    }


}

