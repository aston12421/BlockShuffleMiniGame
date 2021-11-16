package me.aston.blockshuffle.modules;

import me.aston.blockshuffle.BlockShuffle;
import me.aston.blockshuffle.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;
import java.util.UUID;

public final class GameManager {

    private final BlockShuffle plugin = BlockShuffle.getInstance();

    private GameStates gameState = GameStates.INACTIVE;
    private final int roundTime = 300;
    public int roundTimer = roundTime;
    public int mins = (roundTimer % 3600) / 60;
    public int secs = roundTimer % 60;

    public void startGame() {
        //start game
        if (!playerCheck()) {
            Bukkit.broadcastMessage("Not enough players to start the game");
            return;
        }

        gameState = GameStates.ACTIVE;
        playerSetup();
        Bukkit.getServer().broadcastMessage("The game has begun!");

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!playerCheck()) {
                    Bukkit.broadcastMessage("Not enough players to continue the game!");
                    stopGame();
                    this.cancel();
                    return;
                }

                if (roundTimer == 0) {
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        if (plugin.getPlayers().containsKey(online.getUniqueId())) {
                            GamePlayer gamePlayer = plugin.getPlayers().get(online.getUniqueId());
                            if (!gamePlayer.hasFoundBlock()) {
                                eliminatePlayer(gamePlayer.getUuid());
                            }
                        }
                    }

                    if (gameCheck())
                        this.cancel();

                } else {
                    if (roundTimer <= 10)
                        Bukkit.broadcastMessage("You have " + roundTimer + " second(s) to find your block!");

                    roundTimer--;
                    mins = (roundTimer % 3600) / 60;
                    secs = roundTimer % 60;
                    updateScoreBoard();
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }

    public boolean stopGame() {
        if (gameState != GameStates.ACTIVE)
            return false;

        gameState = GameStates.INACTIVE;
        roundTimer = roundTime;
        plugin.getPlayers().clear();
        Bukkit.getScheduler().cancelTasks(plugin);
        for (Player online : Bukkit.getOnlinePlayers())
            online.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

        return true;
    }

    private boolean playerCheck() {
        return Bukkit.getOnlinePlayers().size() > 1;
    }

    private void playerSetup() {
        for (Player online : Bukkit.getOnlinePlayers()) {
            plugin.getPlayers().put(online.getUniqueId(), new GamePlayer(online.getUniqueId(), randomBlock()));
            GamePlayer gamePlayer = plugin.getPlayers().get(online.getUniqueId());
            Player player = Bukkit.getPlayer(gamePlayer.getUuid());
            plugin.getGameboardManager().setGameScoreboard(gamePlayer);
            player.sendMessage("Your block is " + gamePlayer.getMaterial().name() + " good luck!");
        }
    }

    private void eliminatePlayer(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        Bukkit.getServer().broadcastMessage(player.getName() + " has failed to find their block!");
        player.getWorld().createExplosion(player.getLocation(), 10, false, false);
        plugin.getPlayers().remove(uuid);
    }

    private boolean gameCheck() {
        if (plugin.getPlayers().size() == 0) {
            Bukkit.getServer().broadcastMessage("no one wins");
            stopGame();
            return false;
        }

        if (plugin.getPlayers().size() == 1) {
            Player lastStanding = Bukkit.getPlayer(plugin.getPlayers().keySet().stream().findFirst().get());
            Bukkit.getServer().broadcastMessage(lastStanding.getName() + " wins the game!");
            stopGame();
            return false;
        }
        updateBlock();
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

    private void updateBlock() {
        for (GamePlayer gamePlayer : plugin.getPlayers().values()) {
            UUID uuid = gamePlayer.getUuid();
            Player player = Bukkit.getPlayer(uuid);
            gamePlayer.setFound(false);
            gamePlayer.setMaterial(randomBlock());
            player.sendMessage("Your new block is " + gamePlayer.getMaterial().name());
            player.getScoreboard().getTeam("block").setSuffix(ChatColor.BLUE + gamePlayer.getMaterial().name().replace("_", " "));
        }
    }

    private void updateScoreBoard() {
        for (GamePlayer gamePlayer : plugin.getPlayers().values()) {
            Player player = Bukkit.getPlayer(gamePlayer.getUuid());
            player.getScoreboard().getTeam("round_timer").setSuffix(ChatColor.GREEN + Utils.twoDigitString(mins) + ":" + Utils.twoDigitString(secs));
        }
    }

    public GameStates getState() {
        return gameState;
    }

}
