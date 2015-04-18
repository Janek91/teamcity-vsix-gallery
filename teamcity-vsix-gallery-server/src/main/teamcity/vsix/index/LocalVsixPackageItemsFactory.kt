package teamcity.vsix.index

import jetbrains.buildServer.serverSide.SBuild
import java.util.Date
import java.util.LinkedHashMap

public class LocalVsixPackageItemsFactory(finishDate: Date) {
    private val myItems = LinkedHashMap<String, String>()


    public fun createForBuild(build: SBuild): LocalVsixPackageItemsFactory {
        var finishDate = build.getFinishDate()
        if (finishDate == null) finishDate = Date()
        return LocalVsixPackageItemsFactory(finishDate)
    }

    public fun getItems(): Map<String, String> {
        return myItems
    }
}