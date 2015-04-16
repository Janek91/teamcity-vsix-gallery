package settings.tab

import jetbrains.buildServer.web.openapi.*;
import java.util.Arrays

class ServerSettingsTab(pagePlaces: PagePlaces, descriptor: PluginDescriptor) :
        SimpleCustomTab(pagePlaces,
                PlaceId.ADMIN_SERVER_CONFIGURATION_TAB,
                "VsixGalleryServerSettingsTab",
                descriptor.getPluginResourcesPath("settings.jsp"),
                "Extensions Gallery") {
    {
        register()
    }
}

