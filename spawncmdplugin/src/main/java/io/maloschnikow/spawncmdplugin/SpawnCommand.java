package io.maloschnikow.spawncmdplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Random;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import org.jetbrains.annotations.NotNull;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public class SpawnCommand implements BasicCommand {


    private Dictionary<UUID, BukkitRunnable> playerIssuedTeleports;
    private Dictionary<UUID, Long> playerLastUse; // Holds unix time of the last command use of each player
    private final Long COOLDOWN_TIME_SEC;
    private final int FAIL_PROBABILITY; // 1 to x (e.g. 1 to 1000) (kind of)
    private final long TELEPORT_DELAY_TICKS;
    private final Plugin plugin;

    public final String TELEPORT_CANCELLED_MSG;
    public final String TELEPORT_ALREADY_ISSUED_MSG;
    public final String TELEPORT_COOLDOWN_MSG;
    public final String TELEPORT_PROMISE_MSG;
    public final String TELEPORT_RANDOM_FAIL_MSG;

    public SpawnCommand(Plugin plugin) {

        playerLastUse = new Hashtable<>();
        playerIssuedTeleports = new Hashtable<>();
        this.plugin = plugin;
        this.COOLDOWN_TIME_SEC = Long.valueOf(plugin.getConfig().getLong("cooldown-seconds", 60));
        this.TELEPORT_DELAY_TICKS = plugin.getConfig().getInt("delay-ticks", 200);
        this.FAIL_PROBABILITY = plugin.getConfig().getInt("fail-probability", 0);

        this.TELEPORT_CANCELLED_MSG = plugin.getConfig().getString("teleport-cancelled-message", "<red>Teleportation aborted, because of unexpected movement.</red>");
        this.TELEPORT_ALREADY_ISSUED_MSG = plugin.getConfig().getString("teleport-already-issued-message", "<yellow>You will be teleported soon, please standby.</yellow>");
        this.TELEPORT_COOLDOWN_MSG = plugin.getConfig().getString("teleport-cooldown-message", "<red>You'll have to wait <bold><dark_red>%remainingTime%</dark_red></bold> seconds until you can run this command again.</red>");
        this.TELEPORT_PROMISE_MSG = plugin.getConfig().getString("teleport-promise-message", "<green>You will be teleported in <bold><dark_green>%delay%</dark_green></bold> seconds.</green>");
        this.TELEPORT_RANDOM_FAIL_MSG = plugin.getConfig().getString("teleport-random-fail-message", "<red>Not today.</red>");
    }

    public Dictionary<UUID, BukkitRunnable> getPlayerIssuedTeleports() {
        return playerIssuedTeleports;
    }

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {

        // Check if sender is a player
        CommandSender sender = stack.getSender();
        if (!(sender instanceof Player)) {
            return;
        }

        // Get player and world info
        Player player = (Player)stack.getSender();
        UUID uuid = player.getUniqueId();
        
        // Check if player has already issued a teleport
        if (playerIssuedTeleports.get(uuid) != null) {
            player.sendRichMessage(this.TELEPORT_ALREADY_ISSUED_MSG);
            return;
        }
        
        // Check cooldown
        Long lastUse = playerLastUse.get(uuid);
        Long currentTime = Long.valueOf(System.currentTimeMillis());
        if ((lastUse != null) && ( (currentTime - lastUse) < (this.COOLDOWN_TIME_SEC * 1000))) {
            Long remainingTime = (this.COOLDOWN_TIME_SEC) - ((currentTime - lastUse) / 1000);
            player.sendRichMessage(this.TELEPORT_COOLDOWN_MSG.replace("%remainingTime%", remainingTime.toString()));
            return;
        }

        // Determine spawn location
        World spawnWorld = player.getServer().getWorld("world");
        Location vanillaSpawnLocation = spawnWorld.getSpawnLocation();
        // Read spawn location from config
        // If a value is not defined in config, use the default world spawn value
        double x = plugin.getConfig().getDouble("spawn-coordinates.x", vanillaSpawnLocation.getX());
        double y = plugin.getConfig().getDouble("spawn-coordinates.y", vanillaSpawnLocation.getY());
        double z = plugin.getConfig().getDouble("spawn-coordinates.z", vanillaSpawnLocation.getZ());
        float yaw = (float) plugin.getConfig().getDouble("spawn-coordinates.yaw", (double) vanillaSpawnLocation.getYaw());
        float pitch = (float) plugin.getConfig().getDouble("spawn-coordinates.pitch", (double) vanillaSpawnLocation.getPitch());

        Location spawnLocation = new Location(spawnWorld, x, y, z, yaw, pitch);

        // Decides on randomness if player is teleported
        Random rand = new Random();
        int n = rand.nextInt(this.FAIL_PROBABILITY + 1);
        if (n > 0) {
            player.sendRichMessage(this.TELEPORT_PROMISE_MSG.replace("%delay%",Long.valueOf( this.TELEPORT_DELAY_TICKS / 20).toString()));

            // Teleport player with delay

            BukkitRunnable teleportRunnable = new BukkitRunnable() {

                @Override
                public void run() {
                    player.teleportAsync(spawnLocation);
                    playerIssuedTeleports.remove(uuid);
                    // Sets date and time of command use
                    playerLastUse.put(uuid, Long.valueOf(System.currentTimeMillis()));
                }
            };
            Bukkit.getPluginManager().registerEvents(new TeleportToSpawnListener(this), plugin); //do listeners get destroyed? -> of not this could lead to performance issues

            playerIssuedTeleports.put(uuid, teleportRunnable);
            teleportRunnable.runTaskLater(this.plugin, this.TELEPORT_DELAY_TICKS);

        }
        else {
            player.sendRichMessage(this.TELEPORT_RANDOM_FAIL_MSG);
        }
    }
}
