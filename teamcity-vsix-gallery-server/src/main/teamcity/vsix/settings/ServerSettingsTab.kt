package teamcity.vsix.settings

import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildServer.serverSide.impl.ServerSettings
import jetbrains.buildServer.web.openapi.PagePlaces
import jetbrains.buildServer.web.openapi.PlaceId
import jetbrains.buildServer.web.openapi.PluginDescriptor
import jetbrains.buildServer.web.openapi.SimpleCustomTab
import jetbrains.buildServer.web.util.WebUtil
import javax.servlet.http.HttpServletRequest

class ServerSettingsTab(pagePlaces: PagePlaces,
                        val descriptor: PluginDescriptor,
                        val serverSettings: ServerSettings) :
        SimpleCustomTab(pagePlaces,
                PlaceId.ADMIN_SERVER_CONFIGURATION_TAB,
                "vsixGalleryServerSettingsTab",
                descriptor.getPluginResourcesPath("settings.jsp"),
                "VSIX Gallery") {
    {
        addJsFile(descriptor.getPluginResourcesPath("server/feedServer.js"))
        addCssFile(descriptor.getPluginResourcesPath("server/feedServer.css"))

        register()
    }

    override fun fillModel(model: MutableMap<String, Any>, request: HttpServletRequest) {
        super.fillModel(model, request)

        model.put("serverEnabled", true)
        model.put("actualServerUrl", WebUtil.getRootUrl(request));
        model.put("publicFeedUrl", WebUtil.GUEST_AUTH_PREFIX + "/app/vsix/v1/FeedService.svc")
        model.put("isGuestEnabled", serverSettings.isGuestLoginAllowed())
        model.put("imagesUrl", descriptor.getPluginResourcesPath("server/img"))
        model.put("pluginVersion", descriptor.getPluginVersion())
    }
}