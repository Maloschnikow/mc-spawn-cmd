# mc-spawn-cmd

The code and everything works when running ```./gradlew runServer```, but ```./gradlew run``` and ```./gradlew build``` etc. fail.
Also the config for nix is kind of messed up right now and doesn't work as intended.

## Development

### VSCodium and NixOS

#### Required Extensions

1) Name: Gradle for Java
Id: vscjava.vscode-gradle
Description: Manage Gradle Projects, run Gradle tasks and provide better Gradle file authoring experience in VS Code
Version: 3.15.0
Publisher: vscjava
VS Marketplace Link: https://open-vsx.org/vscode/item?itemName=vscjava.vscode-gradle
2) Name: Nix Environment Selector
Id: arrterian.nix-env-selector
Description: Allows switch environment for Visual Studio Code and extensions based on Nix config file.
Version: 1.0.11
Publisher: arrterian
VS Marketplace Link: https://open-vsx.org/vscode/item?itemName=arrterian.nix-env-selector
3) Name: Kotlin
Id: fwcd.kotlin
Description: Smart code completion, debugging, linting, syntax highlighting and more for Kotlin
Version: 0.2.35
Publisher: fwcd
VS Marketplace Link: https://open-vsx.org/vscode/item?itemName=fwcd.kotlin

#### Setup
Select via the Nix Environment Selector the shell.nix and restart VSCodium. (F1 -> Nix-Env: Select environment -> shell.nix)

#### Start a server for testing
```./gradlew runServer```