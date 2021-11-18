package me.aston.shuffle.game;

import me.aston.shuffle.BlockShuffle;
import me.aston.shuffle.game.player.GamePlayer;
import me.aston.shuffle.game.player.GamePlayerManager;
import me.aston.shuffle.game.state.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public final class Game {

    private final BlockShuffle plugin = BlockShuffle.getInstance();
    private final GamePlayerManager playerManager = BlockShuffle.getInstance().getGamePlayerManager();
    private final List<Material> blocks = Arrays.stream(Material.values()).filter(mat -> mat.isBlock() && mat.isSolid()).collect(Collectors.toList());
    private GameState state = GameState.INACTIVE;
    private int defaultRoundTimer = 300; //5 minutes
    private int timer = defaultRoundTimer;
    public Game() {
    }

    public boolean start() {
        if (!enoughPlayers()) return false; //Check if there are enough players to start the game

        setState(GameState.ACTIVE); //Change the state of the game to active
        setTimer(getDefaultRoundTimer()); //Reset the timer
        setupPlayers(); //Setup the player Map containing the player and their block

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (getState() == GameState.INACTIVE)
                return;

            if (getTimer() == 0) {

                roundCheck();
                eliminateLosers(); // Eliminate those who haven't found their block
                if (roundCheck()) // If there are still more than 1 player and they have both found their block
                    updateBlock(); // Update their block and start a new round

            } else {
                if (!enoughPlayers()) {
                    stop();
                    return;
                }

                if (getTimer() <= 10) {
                    playerManager.broadcast("There are " + getTimer() + " second(s) remaining!");
                }
                setTimer(getTimer() - 1);
            }
        }, 20L, 20L);

        return true;
    }

    public void stop() {
        Bukkit.getScheduler().cancelTasks(plugin);
        playerManager.clear();
        setState(GameState.INACTIVE);
    }


    private boolean roundCheck() {
        if (playerManager.size() == 1 || playerManager.size() == 0) {
            Bukkit.getOnlinePlayers().forEach(online -> online.sendMessage((playerManager.getLast() == null ? "no one" : playerManager.getLast().getPlayer().getName()) + " wins!"));
            stop();
        }
        return playerManager.size() > 1;
    }

    private void updateBlock() {
        Bukkit.getOnlinePlayers().forEach(online -> {
            Optional<GamePlayer> optionalGamePlayer = playerManager.find(online.getUniqueId());

            optionalGamePlayer.ifPresent(gamePlayer -> {
                gamePlayer.setFoundBlock(false);
                gamePlayer.setMaterial(randomBlock());
                gamePlayer.getPlayer().getInventory().addItem(new ItemStack(gamePlayer.getMaterial()));
                gamePlayer.sendMessage("&bYour block is &e" + gamePlayer.getMaterial().name());
                setTimer(getDefaultRoundTimer());
            });
        });
    }


    private void eliminateLosers() {
        Bukkit.getOnlinePlayers().forEach(online -> {
            Optional<GamePlayer> optionalGamePlayer = playerManager.find(online.getUniqueId());

            optionalGamePlayer.ifPresent(gamePlayer -> {
                if (!gamePlayer.hasFoundBlock()) {
                    gamePlayer.sendMessage("&cYou did not find your block in time!\n&eYou have been eliminated!");
                    playerManager.remove(online.getUniqueId());
                }
            });

        });
    }

    private void setupPlayers() {
        playerManager.clear();
        Bukkit.getOnlinePlayers().forEach(online -> {
            playerManager.register(online.getUniqueId());

            Optional<GamePlayer> optionalGamePlayer = playerManager.find(online.getUniqueId());

            optionalGamePlayer.ifPresent(gamePlayer -> {
                gamePlayer.setMaterial(randomBlock());
                gamePlayer.getPlayer().getInventory().addItem(new ItemStack(gamePlayer.getMaterial()));
                gamePlayer.sendMessage("&bYour block is &e" + gamePlayer.getMaterial().name());
            });

        });
    }

    private boolean enoughPlayers() {
        return Bukkit.getOnlinePlayers().size() > 1;
    }

    private Material randomBlock() {
        return blocks.get(new Random().nextInt(blocks.size()));
    }


    @NotNull
    public GameState getState() {
        return state;
    }

    public void setState(@NotNull final GameState state) {
        this.state = state;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(final int timer) {
        this.timer = timer;
    }

    public int getDefaultRoundTimer() {
        return defaultRoundTimer;
    }

    public void setDefaultRoundTimer(final int defaultRoundTimer) {
        this.defaultRoundTimer = defaultRoundTimer;
    }


}
