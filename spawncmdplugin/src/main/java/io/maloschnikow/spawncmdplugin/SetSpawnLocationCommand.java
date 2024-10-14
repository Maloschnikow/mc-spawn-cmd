package io.maloschnikow.spawncmdplugin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;


public class SetSpawnLocationCommand implements Command<CommandSourceStack> {

    private final Plugin plugin;

    public SetSpawnLocationCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        CommandSourceStack stack = (CommandSourceStack) context.getSource();

        // Check if sender is a player and if so check permission
        CommandSender sender = stack.getSender();
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(!p.hasPermission("permissions.setspawnlocation")) {
                p.sendPlainMessage("You don't have permission to run this command!");
                return 0;
            }
        }
        
        FinePositionResolver pos = (FinePositionResolver) context.getArgument("coordinates", FinePositionResolver.class);

        double x = pos.resolve(stack).x();
        double y = pos.resolve(stack).y();
        double z = pos.resolve(stack).z();
    
        this.plugin.getConfig().set("spawn-coordinates.x", x);
        this.plugin.getConfig().set("spawn-coordinates.y", y);
        this.plugin.getConfig().set("spawn-coordinates.z", z);
        
        //todo need to check how to make more arguments
        /* if (args.length >= 4) {
            double yaw = Double.parseDouble(args[3]);
            this.plugin.getConfig().set("spawn-coordinates.yaw", yaw);
        }
        if (args.length >= 5) {
            double pitch = Double.parseDouble(args[4]);    
            this.plugin.getConfig().set("spawn-coordinates.pitch", pitch);
        } */

        this.plugin.saveConfig();
        sender.sendMessage("New spawn location set.");
        return Command.SINGLE_SUCCESS;
    }
}
