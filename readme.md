
#TeamCity Visual Studio Gallery (vsix) Plugin

This plugin turns your TeamCity 9 into a private Visual Studio Extension Gallery!

![](http://i.imgur.com/NeUKH1A.png)

![](http://i.imgur.com/8pI0IMh.png)

## Installation

* Download the latest `teamcity-vsix-gallery.zip` from the [releases](../..//releases) page
* Put the zip file into your [TeamCity Data](https://confluence.jetbrains.com/display/TCD9/TeamCity+Data+Directory) `plugins` directory (e.g. `D:\TeamCity\data\plugins`)
* Restart the TeamCity service. The plugin will be automatically extracted and installed

## Configuration

If the plugin is installed successfully, you should see a new menu under **Integrations**:  
![](http://i.imgur.com/0qmnL8Y.png)

Before using the plugin, however, several operations must be performed:

1. Enable the **guest** account: Visual Studio does not support authenticated feeds, so a guest access must be enabled before using the VSIX Gallery.
  * In your TeamCity, open the **Administration** page, and go to **Authentication**, located under **Server Administration**
  * Make sure *Allow login as guest user* is checked:  
  ![](http://i.imgur.com/2MSK4cO.png)
  
2. Configure your project to include your `.vsix` as a build artifact:
  * Go to your build configuration, and in the **Artifact path**, make sure your `.vsix` is copied to the artifacts root, e.g.:
  ![](http://i.imgur.com/6eYH1vt.png)

3. Reset the `buildsMetadata` cache:
  * Go to **Diagnostics**, and click the **Caches** tab
  * Click **Reset** next to the `buildsMetadata` entry

After this, go to the new **VSIX Gallery** menu, and you should see the feed to the gallery, e.g.:  
![](http://i.imgur.com/36Wgi8x.png)

Copy this link, and add it to the **Extensions and Updates** in the **Tools - Options** dialog in your Visual Studio (as pictured above)

That's it! From now on, every new build will be automatically indexed, and appear as an update in your gallery! 

## Build

The plugin is written in a mixture of Kotlin and Java, and built with IntelliJ IDEA 14 Community with the Kotlin plugin.
Building is done via maven.

If you want to build the plugin yourself, you need to define an environment variable called `TEAMCITY_DATA_DIRECTORY`, pointing to your TeamCity Data directory.
The `install` phase of this plugin takes the packaged zip file and copies it into the `plugins` directory under the TeamCity Data directory, and restarts the TeamCity service!

## Bugs? Questions? Suggestions?

Please feel free to [report them](../../issues) and send me a pull request!
