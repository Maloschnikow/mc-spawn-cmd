# mc-spawn-cmd

## Development

### VSCodium and NixOS

#### Required Extensions

1) Name: Language Support for Java(TM) by Red Hat
Id: redhat.java
Description: Java Linting, Intellisense, formatting, refactoring, Maven/Gradle support and more...
Version: 1.35.1
Publisher: redhat
VS Marketplace Link: https://open-vsx.org/vscode/item?itemName=redhat.java
2) Name: Nix Environment Selector
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