package io.maloschnikow.spawncmdplugin;

import org.bukkit.plugin.java.JavaPlugin;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.plugin.Plugin;


public class SpawnCMDPlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        saveDefaultConfig();
        
        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register("spawn", new SpawnCommand(this));
            commands.register("setspawnlocation", new SetSpawnLocationCommand(this));
        });
    }

}