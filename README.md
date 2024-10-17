# mc-spawn-cmd

## Use
### First installation
1. Download the compiled `.jar` file from the latest [Release](https://github.com/Maloschnikow/mc-spawn-cmd/releases) matching your Minecraft Version.
2. Put the file in your `plugins` folder of your server's root directory.
3. Restart your server. The plugin will now generate a `config.yml` file in the `plugins/spawncmdplugin` folder.
4. Use the configuration file mentioned above to configure the plugin to your liking.
5. Restart your server to apply changes made to `config.yml`

> [!TIP]
> You may also want to read https://docs.papermc.io/paper/adding-plugins.

### Update
1. Remove the `.jar` file of the old version from the `plugins` folder.
2. To get changes made to the `config.yml` you need to delete or **rename** the `config.yml` contained in your server's `plugins/spawncmdplugin` folder. (Renaming the file is recommended)
3. Download the compiled `.jar` file from the latest [Release](https://github.com/Maloschnikow/mc-spawn-cmd/releases) matching your Minecraft Version.
4. Put the file in your `plugins` folder of your server's root directory.
5. Restart your server. The plugin will now generate a **new** `config.yml` file in the `plugins/spawncmdplugin` folder.
6. If you renamed the old `config.yml` in step 2, you can now copy the changes you made to the **old** `config.yml` into the **new** `config.yml` and won't have to reconbfigure it all from scratch.
7. Delete your **old** `config.yml`.


## Development

### VSCodium and NixOS

#### Required Extensions

1. > Name: Language Support for Java(TM) by Red Hat
Id: redhat.java
Description: Java Linting, Intellisense, formatting, refactoring, Maven/Gradle support and more...
Version: 1.35.1
Publisher: redhat
VS Marketplace Link: https://open-vsx.org/vscode/item?itemName=redhat.java

2. >Name: Nix Environment Selector
Id: arrterian.nix-env-selector
Description: Allows switch environment for Visual Studio Code and extensions based on Nix config file.
Version: 1.0.11
Publisher: arrterian
VS Marketplace Link: https://open-vsx.org/vscode/item?itemName=arrterian.nix-env-selector


#### Setup
Select via the Nix Environment Selector the shell.nix and restart VSCodium. (F1 -> Nix-Env: Select environment -> shell.nix)
Enter a ```nix-shell``` and run ```./gradlew```.

To get the java language support to work you need to run echo ```$JAVA_HOME``` inside the nix shell, copy the output string and paste it into .vscode/settings.json under ```java.jdt.ls.java.home```.

#### Start a server for testing
Run ```./gradlew runServer```. This task will fail.
Edit the spawncmdplugin/run/eula.txt file: Change ```false``` to ```true```

#### Build
Run ```./gradlew build```.
The built .jar file will be in spawncmdplugin/build/libs.