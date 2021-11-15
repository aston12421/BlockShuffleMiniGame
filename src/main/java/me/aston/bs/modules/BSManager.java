package me.aston.bs.modules;

import me.aston.bs.BS;
import me.aston.bs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Random;
import java.util.UUID;

public class BSManager {

    private final BS plugin = BS.getInstance();

    private GameStates gameState = GameStates.INACTIVE;
    public int roundTimer;

    public void startGame() {
        //start game
        if (!setupPlayers()) {
            Bukkit.broadcastMessage("Not enough players to start the game");
            return;
        }
        gameState = GameStates.ACTIVE;
        roundTimer = 300;
        Bukkit.getServer().broadcastMessage("The game has begun!");

        new BukkitRunnable() {
            @Override
            public void run() {
                if (roundTimer == 0) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (plugin.getPlayers().containsKey(player.getUniqueId())) {
                            foundCheck(player.getUniqueId());
                        }
                    }
                    if (gameCheck()) {
                        this.cancel();
                    }
                } else {
                    if (roundTimer <= 10) {
                        Bukkit.broadcastMessage("You have " + roundTimer + " second(s) to find your block!");
                    }
                    roundTimer--;
                    int mins = (roundTimer % 3600) / 60;
                    int secs = roundTimer % 60;
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (plugin.getPlayers().containsKey(player.getUniqueId())) {
                            player.getScoreboard().getTeam("round_timer").setSuffix(ChatColor.GREEN + Utils.twoDigitString(mins) + ":" + Utils.twoDigitString(secs));
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }

    public void stopGame() {
        if (gameState != GameStates.ACTIVE)
            return;
        plugin.getPlayers().clear();
        Bukkit.getScheduler().cancelTasks(plugin);
        gameState = GameStates.INACTIVE;
        Bukkit.getServer().broadcastMessage("The game has been stopped!");
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
    }

    private boolean setupPlayers() {
        if (Bukkit.getOnlinePlayers().size() <= 1) {
            return false;
        }
        for (Player online : Bukkit.getOnlinePlayers()) {
            plugin.getPlayers().put(online.getUniqueId(), new BSPlayer(online.getUniqueId(), randomBlock()));
            BSPlayer bsPlayer = plugin.getPlayers().get(online.getUniqueId());
            plugin.getSbManager().create(bsPlayer);
            Player player = Bukkit.getPlayer(bsPlayer.getUuid());
            player.sendMessage("Your block is " + bsPlayer.getMaterial().name() + " good luck!");
        }
        return true;
    }

    private boolean foundCheck(UUID uuid) {
        BSPlayer bsPlayer = plugin.getPlayers().get(uuid);

        if (!(bsPlayer.hasFoundBlock())) {
            Player player = Bukkit.getPlayer(uuid);
            Bukkit.getServer().broadcastMessage(player.getName() + " has failed to find their block!");
            player.getWorld().createExplosion(player.getLocation(), 10, false, false);
            plugin.getPlayers().remove(uuid);
            return false;
        }
        return true;
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
        for (BSPlayer bsPlayer : plugin.getPlayers().values()) {
            UUID uuid = bsPlayer.getUuid();
            Player player = Bukkit.getPlayer(uuid);
            bsPlayer.setFound(false);
            bsPlayer.setMaterial(randomBlock());
            player.sendMessage("Your new block is " + bsPlayer.getMaterial().name());
            player.getScoreboard().getTeam("block").setSuffix(ChatColor.BLUE + bsPlayer.getMaterial().name().replace("_", " "));
        }
    }

    public GameStates getState() {
        return gameState;
    }

}
