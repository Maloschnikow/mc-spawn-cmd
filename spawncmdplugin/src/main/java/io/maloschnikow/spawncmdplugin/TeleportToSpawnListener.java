package io.maloschnikow.spawncmdplugin;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportToSpawnListener implements Listener {

    private final SpawnCommand spawnCommand;
    
    public TeleportToSpawnListener(SpawnCommand spawnCommand) {
        this.spawnCommand = spawnCommand;
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
