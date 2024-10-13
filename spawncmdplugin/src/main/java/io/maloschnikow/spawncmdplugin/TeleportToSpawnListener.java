package io.maloschnikow.spawncmdplugin;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportToSpawnListener implements Listener {

    SpawnCommand spawnCommand;
    
    public TeleportToSpawnListener(SpawnCommand spawnCommand) {
        this.spawnCommand = spawnCommand;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        BukkitRunnable l = spawnCommand.getPlayerIssuedTeleports().get(uuid);
        if (l == null) {
            return;
        }
        l.cancel();
        spawnCommand.getPlayerIssuedTeleports().remove(uuid);
        event.getPlayer().sendRichMessage("<red>Teleportation abgebrochen, aufgrund von unvorhergesehener Bewegung.</red>");
    }
}
