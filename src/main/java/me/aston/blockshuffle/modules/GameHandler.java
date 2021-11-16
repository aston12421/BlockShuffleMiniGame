package me.aston.blockshuffle.modules;

import me.aston.blockshuffle.BlockShuffle;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public final class GameHandler implements Listener {

    private final BlockShuffle plugin = BlockShuffle.getInstance();

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (plugin.getGameManager().getState() != GameStates.ACTIVE)
            return;

        Player player = event.getPlayer();

        if (!(plugin.getPlayers().containsKey(player.getUniqueId())))
            return;

        GamePlayer gamePlayer = plugin.getPlayers().get(player.getUniqueId());
        Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        if (block.getType() == gamePlayer.getMaterial()) {
            if (!(gamePlayer.hasFoundBlock())) {
                gamePlayer.setFound(true);
                Bukkit.getServer().broadcastMessage(player.getName() + " has found their block!");
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (plugin.getGameManager().getState() != GameStates.ACTIVE)
            return;

        if (!plugin.getPlayers().containsKey(uuid))
            return;

        GamePlayer gamePlayer = plugin.getPlayers().get(uuid);
        plugin.getGameboardManager().setGameScoreboard(gamePlayer);
    }
}
