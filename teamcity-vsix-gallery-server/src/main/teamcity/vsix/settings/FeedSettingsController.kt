package teamcity.vsix.settings

import jetbrains.buildServer.controllers.BaseController
import jetbrains.buildServer.serverSide.impl.ServerSettings
import jetbrains.buildServer.web.openapi.PluginDescriptor
import jetbrains.buildServer.web.openapi.WebControllerManager
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class FeedSettingsController(val web: WebControllerManager,
                             val descriptor: PluginDescriptor,
                             val settings: VsixServerSettings,
                             val serverSettings: ServerSettings) : BaseController() {
    {
        web.registerController(settings.IncludePath, this);

    }
    override fun doHandle(request: HttpServletRequest, response: HttpServletResponse): ModelAndView? {
        val mv = ModelAndView(descriptor.getPluginResourcesPath("server/feedServerSettingsWindows.jsp"))

        return mv
    }


}