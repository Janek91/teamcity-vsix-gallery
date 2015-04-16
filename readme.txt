
TeamCity Visual Studio Gallery (vsix) Plugin

1. Build
Issue 'mvn package' command from the root project to build the plugin. Resulting package, teamcity-vsix-gallery.zip, will be copied to the plugins directory,
under TEAMCITY_DATA_PATH environment variable (so make sure that's defined).

The package step will also restart the TeamCity service!