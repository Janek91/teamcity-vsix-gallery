package settings.tab

import jetbrains.buildServer.web.openapi.*;
import java.util.Arrays

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

