package io.maloschnikow.spawncmdplugin;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportToSpawnListener implements Listener {

    private final SpawnCommand spawnCommand;
    private final Plugin plugin;

    // Vars for configuration
    private final boolean TP_ON_PLAYER_JOIN;
    
    public TeleportToSpawnListener(Plugin plugin, SpawnCommand spawnCommand) {
        this.spawnCommand = spawnCommand;
        this.plugin = plugin;

        // Load configuration
        this.TP_ON_PLAYER_JOIN = plugin.getConfig().getBoolean("tp-on-player-join", false);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!this.TP_ON_PLAYER_JOIN) { return; }
        World spawnWorld = event.getPlayer().getServer().getWorld("world");
        Location spawnLocation = spawnCommand.getSpawnLocation(this.plugin, spawnWorld);
        event.getPlayer().teleportAsync(spawnLocation);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        BukkitRunnable l = spawnCommand.getPlayerIssuedTeleports().get(uuid);
        if (l == null) {
            return;
        }
        l.cancel();
        spawnCommand.getPlayerIssuedTeleports().remove(uuid);
        player.sendRichMessage(this.spawnCommand.TELEPORT_CANCELLED_MSG);
        if(spawnCommand.SOUNDS_ENABLED) { player.playSound(player, spawnCommand.TELEPORT_CANCELLED_SOUND, 1.0f, 1.0f); }
    }
}
