package me.aston.shuffle.game.listeners;

import me.aston.shuffle.BlockShuffle;
import me.aston.shuffle.game.player.GamePlayer;
import me.aston.shuffle.game.state.GameState;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.Optional;

public class GameListener implements Listener {

    private final BlockShuffle plugin = BlockShuffle.getInstance();

    public GameListener() {
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        if (plugin.getGame().getState() == GameState.INACTIVE) return;

        Player player = event.getPlayer();

        Optional<GamePlayer> optionalGamePlayer = plugin.getGamePlayerManager().find(player.getUniqueId());

        optionalGamePlayer.ifPresent(gamePlayer -> {
            if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == gamePlayer.getMaterial() ||
                    player.getLocation().getBlock().getRelative(BlockFace.SELF).getType() == gamePlayer.getMaterial()) {

                if (!gamePlayer.hasFoundBlock()) {
                    gamePlayer.setFoundBlock(true);

                    plugin.getGamePlayerManager().broadcast("&b" + player.getName() + " &ejust found their block!");
                }
            }
        });
    }
}
