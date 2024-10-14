package io.maloschnikow.spawncmdplugin;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public class SetSpawnLocationCommand implements BasicCommand {

    private final Plugin plugin;

    public SetSpawnLocationCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
        public String permission() {
            return "permissions.setspawnlocation";
        }

    @Override
    public void execute(CommandSourceStack stack, String[] args) {

        // Check if sender is a player and if so check permission
        CommandSender sender = stack.getSender();

        if (args.length < 3) {
            sender.sendMessage("Not enough arguments provided. See /help.");
            return;
        }
        try {
            double x = Double.parseDouble(args[0]);
            double y = Double.parseDouble(args[1]);
            double z = Double.parseDouble(args[2]);
        
            this.plugin.getConfig().set("spawn-coordinates.x", x);
            this.plugin.getConfig().set("spawn-coordinates.y", y);
            this.plugin.getConfig().set("spawn-coordinates.z", z);
            
            if (args.length >= 4) {
                double yaw = Double.parseDouble(args[3]);
                this.plugin.getConfig().set("spawn-coordinates.yaw", yaw);
            }
            if (args.length >= 5) {
                double pitch = Double.parseDouble(args[4]);    
                this.plugin.getConfig().set("spawn-coordinates.pitch", pitch);
            }
        } catch (NumberFormatException e) {
            sender.sendMessage("The specified values are not applicable.");
            return;
        }
        this.plugin.saveConfig();
        sender.sendMessage("New spawn location set.");
    }
}
