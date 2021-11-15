package me.aston.bs.modules;

import me.aston.bs.BS;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class BSHandler implements Listener {

    private final BS plugin = BS.getInstance();

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (plugin.getBsManager().getState() != GameStates.ACTIVE)
            return;

        Player player = event.getPlayer();

        if (!(plugin.getPlayers().containsKey(player.getUniqueId())))
            return;

        BSPlayer bsPlayer = plugin.getPlayers().get(player.getUniqueId());
        Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        if (block.getType() == bsPlayer.getMaterial()) {
            if (!(bsPlayer.hasFoundBlock())) {
                bsPlayer.setFound(true);
                Bukkit.getServer().broadcastMessage(player.getName() + " has found their block!");
            }
        }
    }
}
