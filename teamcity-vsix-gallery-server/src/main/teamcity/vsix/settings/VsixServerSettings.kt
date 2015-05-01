package teamcity.vsix.settings

import jetbrains.buildServer.web.openapi.PluginDescriptor

class VsixServerSettings(val descriptor: PluginDescriptor) {
    val VsixFeedPath = "/app/vsix/v1/FeedService.svc"

    val IncludePath = descriptor.getPluginResourcesPath("feed/status.html")
    val JsIncludes = setOf(descriptor.getPluginResourcesPath("server/feedServer.js"))
    val CssIncludes = setOf(descriptor.getPluginResourcesPath("server/feedServer.css"))
}