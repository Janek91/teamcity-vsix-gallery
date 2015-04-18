package teamcity.vsix.settings

import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildServer.web.openapi.PagePlaces
import jetbrains.buildServer.web.openapi.PlaceId
import jetbrains.buildServer.web.openapi.PluginDescriptor
import jetbrains.buildServer.web.openapi.SimpleCustomTab

class ServerSettingsTab(pagePlaces: PagePlaces, descriptor: PluginDescriptor) :
        SimpleCustomTab(pagePlaces,
                PlaceId.ADMIN_SERVER_CONFIGURATION_TAB,
                "vsixGalleryServerSettingsTab",
                descriptor.getPluginResourcesPath("settings.jsp"),
                "VSIX Gallery") {
    {
        register()
    }
}