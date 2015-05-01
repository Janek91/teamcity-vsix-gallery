package teamcity.vsix.feed

import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildServer.controllers.BaseController
import jetbrains.buildServer.web.openapi.PluginDescriptor
import jetbrains.buildServer.web.openapi.WebControllerManager
import org.springframework.web.servlet.ModelAndView
import teamcity.vsix.settings.VsixServerSettings
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class VsixFeedController(val web : WebControllerManager,
                         val feedHandler : AtomFeedCreator,
                         val settings: VsixServerSettings) : BaseController() {

    {
        web.registerController(settings.VsixFeedPath + "/**", this)
    }

    override fun doHandle(request: HttpServletRequest, response: HttpServletResponse): ModelAndView? {

        feedHandler.handleRequest(request, response)
        return null
    }
}

