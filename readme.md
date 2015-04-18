
TeamCity Visual Studio Gallery (vsix) Plugin

# Build
1. Register a maven dependency of 'server.jar' into your local Maven repository, by running:
```
    mvn install:install-file -Dfile="{TEAMCITY_ROOT}/webapps/ROOT/WEB-INF/lib/server.jar" -DgroupId="org.jetbrains.teamcity" -DartifactId="server" -Dversion=9.0.1 -Dpackaging=jar
```

2. Set an environment variable `TEAMCITY_DATA_PATH`, pointing to the [TeamCity Data Directory](https://confluence.jetbrains.com/display/TCD9/TeamCity+Data+Directory).
3. Run 'mvn package' command from the root project to build the plugin. Resulting package, `teamcity-vsix-gallery.zip`, will be copied to the plugins directory.

The package step will also restart the TeamCity service!
