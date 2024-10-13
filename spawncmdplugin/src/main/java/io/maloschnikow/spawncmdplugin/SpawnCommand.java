package io.maloschnikow.spawncmdplugin;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public class SpawnCommand implements BasicCommand {

    private Dictionary<UUID, Long> playerLastUse; //Holds unix time in seconds of the last command use of each player
    private final Long COOLDOWN_TIME_SEC = 60L;
    private final int FAIL_PROBABILITY = 1000; // 1 to x (e.g. 1 to 1000) (kind of)
    private final long TELEPORT_DELAY_SEC = 6;

    public SpawnCommand() {

        playerLastUse = new Hashtable<>();

    }

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        CommandSender sender = stack.getSender();
        if (!(sender instanceof Player)) {
            return;
        }

        //Get player and world info
        Player player = (Player)stack.getSender();
        UUID uuid = player.getUniqueId();
        World spawnWorld = player.getServer().getWorld("world");
        Location spawnLocation = spawnWorld.getSpawnLocation();

        //Check cooldown
        Long lastUse = playerLastUse.get(uuid);
        Long currentTime = Long.valueOf(System.currentTimeMillis() / 1000L);
        if ((lastUse != null) && ( (currentTime - lastUse) < COOLDOWN_TIME_SEC)) {
            Long remainingTime = COOLDOWN_TIME_SEC - (currentTime - lastUse);
            player.sendRichMessage("<red>Warte noch <bold><dark_red>" + remainingTime.toString() + "</dark_red></bold> Sekunden.</red>");
            return;
        }

        //Sets date and time of command use
        this.playerLastUse.put(uuid, Long.valueOf(System.currentTimeMillis() / 1000L));

        //Decides on randomness if player is teleported
        Random rand = new Random();
        int n = rand.nextInt(FAIL_PROBABILITY + 1);
        if (n > 0) {
            player.sendRichMessage("<green>Du wirst in <bold><dark_green>" + TELEPORT_DELAY_SEC +"</dark_green></bold> Sekunden teleportiert.</green>");
            
            //Teleport player with delay
            final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.schedule(new Runnable() {
                @Override
                public void run() {
                    player.teleportAsync(spawnLocation);
                }
            }, TELEPORT_DELAY_SEC, TimeUnit.SECONDS);

        }
        else {
            player.sendRichMessage("<red>Ne, heute nicht.</red>");
        }
    }
}
