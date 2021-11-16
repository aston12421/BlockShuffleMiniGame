package me.aston.shuffle.game;

import me.aston.shuffle.BlockShuffle;
import me.aston.shuffle.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;
import java.util.UUID;

public final class GameManager {

    private final BlockShuffle plugin = BlockShuffle.getInstance();
    private final Game game = plugin.getGame();

    public void startGame() {
        if (!enoughPlayers()) {
            Bukkit.broadcastMessage("there arent enough players to start the game!");
            return;
        }

        game.setGameState(GameState.ACTIVE);
        game.resetRoundTimer();
        addPlayers();

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (!enoughPlayers()) {
                Bukkit.broadcastMessage("there are not enough players online for the game to continue.");
                stopGame();
                return;
            }

            if (game.getRoundTimer() == 0) {
                eliminateLosers();

                if (!gameCheck()) {
                    stopGame();
                    return;
                }

                updateBlock();
                updateScoreBoard();
                game.resetRoundTimer();
            } else {
                if (game.getRoundTimer() <= 10)
                    Bukkit.broadcastMessage("You have " + game.getRoundTimer() + " more second(s) to find your block!");
                game.setRoundTimer(game.getRoundTimer() -1);
                updateScoreBoard();
            }

        }, 20L, 20L);

    }

    public void stopGame() {
        Bukkit.getScheduler().cancelTasks(plugin);
        game.setGameState(GameState.INACTIVE);
        game.getPlayers().clear();
        clearScoreboard();
    }

    private boolean enoughPlayers() {
        return Bukkit.getOnlinePlayers().size() > 1;
    }

    private void addPlayers() {
        for (Player online : Bukkit.getOnlinePlayers()) {
            game.getPlayers().put(online.getUniqueId(), new GamePlayer(online.getUniqueId(), randomBlock()));
            GamePlayer gamePlayer = game.getPlayers().get(online.getUniqueId());
            Player player = Bukkit.getPlayer(gamePlayer.getUuid());
            Bukkit.getPlayer(gamePlayer.getUuid()).setScoreboard(plugin.getGameboard().getGameScoreboard());
            player.getScoreboard().getTeam("block").setSuffix(ChatColor.BLUE + gamePlayer.getMaterial().name().replace("_", " "));
            player.getInventory().addItem(new ItemStack(gamePlayer.getMaterial()));
            player.sendMessage("Your block is " + gamePlayer.getMaterial().name() + " good luck!");
        }
    }

    private boolean gameCheck() {
        if (game.getPlayers().size() == 0) {
            Bukkit.getServer().broadcastMessage("no one wins");
            return false;
        }

        if (game.getPlayers().size() == 1) {
            Player lastStanding = Bukkit.getPlayer(game.getPlayers().keySet().stream().findFirst().get());
            Bukkit.getServer().broadcastMessage(lastStanding.getName() + " wins the game!");
            return false;
        }
        return true;
    }

    private Material randomBlock() {
        Material material = null;
        Random random = new Random();
        while(material == null)
        {
            material = Material.values()[random.nextInt(Material.values().length)];
            if(!(material.isBlock() && material.isSolid()))
            {
                material = null;
            }
        }
        return material;
    }

    private void eliminateLosers() {
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (game.getPlayers().containsKey(online.getUniqueId())) {
                GamePlayer gamePlayer = game.getPlayers().get(online.getUniqueId());

                if (!gamePlayer.hasFoundBlock()) {
                    online.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                    online.getWorld().createExplosion(online.getLocation(), 0, false, false);
                    online.setHealth(0);
                    Bukkit.getServer().broadcastMessage(online.getName() + " has failed to find their block!");
                    game.getPlayers().remove(online.getUniqueId());
                }
            }
        }
    }

    private void updateBlock() {
        for (GamePlayer gamePlayer : game.getPlayers().values()) {
            UUID uuid = gamePlayer.getUuid();
            Player player = Bukkit.getPlayer(uuid);
            gamePlayer.setFound(false);
            gamePlayer.setMaterial(randomBlock());
            player.sendMessage("Your new block is " + gamePlayer.getMaterial().name());
            player.getScoreboard().getTeam("block").setSuffix(ChatColor.BLUE + gamePlayer.getMaterial().name().replace("_", " "));
        }
    }

    private void updateScoreBoard() {
        for (GamePlayer gamePlayer : game.getPlayers().values()) {
            Player player = Bukkit.getPlayer(gamePlayer.getUuid());
            player.getScoreboard().getTeam("round_timer").setSuffix(ChatColor.GREEN + Utils.twoDigitString(game.getMins()) + ":" + Utils.twoDigitString(game.getSecs()));
            if (gamePlayer.hasFoundBlock()) {
                player.getScoreboard().getTeam("found_block").setPrefix(ChatColor.GREEN + "FOUND");
            } else {
                player.getScoreboard().getTeam("found_block").setPrefix(ChatColor.GRAY + "FOUND");
            }
        }
    }

    private void clearScoreboard() {
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
    }
}
