package me.aston.shuffle.game;

import me.aston.shuffle.BlockShuffle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public final class GameListener implements Listener {

    private final BlockShuffle plugin = BlockShuffle.getInstance();
    private final Game game = plugin.getGame();

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (game.getGameState() != GameState.ACTIVE)
            return;

        Player player = event.getPlayer();

        if (!(game.getPlayers().containsKey(player.getUniqueId())))
            return;

        GamePlayer gamePlayer = game.getPlayers().get(player.getUniqueId());
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

        if (game.getGameState() != GameState.ACTIVE)
            return;

        if (!game.getPlayers().containsKey(uuid))
            return;

        player.setScoreboard(plugin.getGameboard().getGameScoreboard());
    }
}
