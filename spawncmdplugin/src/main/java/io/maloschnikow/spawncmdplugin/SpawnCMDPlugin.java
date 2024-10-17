package io.maloschnikow.spawncmdplugin;

import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.plugin.Plugin;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;


public class SpawnCMDPlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        saveDefaultConfig();

        SpawnCommand spawnCommand = new SpawnCommand(this);
        getServer().getPluginManager().registerEvents(new TeleportToSpawnListener(this, spawnCommand), this);
        
        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();

            // Register /spawn command
            commands.register(
                Commands.literal("spawn")
                    .executes(spawnCommand)
                .build(),
                "Teleports the player back to spawn."
            );

            // Register /setspawnlocation command
            commands.register(
                Commands.literal("setspawnlocation")
                .then(
                    Commands.argument("coordinates", ArgumentTypes.finePosition())
                        .executes(new SetSpawnLocationCommand(this))
                ).requires(new Permission("permissions.setspawnlocation")
                ).build(),
                "Set the spawn location which should be used by this plugin."
            );
        });
    }

}