package me.aston.blockshuffle;

import me.aston.blockshuffle.commands.GameCommand;
import me.aston.blockshuffle.modules.GameHandler;
import me.aston.blockshuffle.modules.GameManager;
import me.aston.blockshuffle.modules.GamePlayer;
import me.aston.blockshuffle.modules.GameboardManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class BlockShuffle extends JavaPlugin {

    private static BlockShuffle instance;
    private Map<UUID, GamePlayer> players;
    private GameManager gameManager;
    private GameboardManager gameboardManager;

    @Override
    public void onEnable() {
        instance = this;
        players = new HashMap<>();
        gameManager = new GameManager();
        gameboardManager = new GameboardManager();

        registerEvents();
        registerCommands();
    }

    @Override
    public void onDisable() {
        getGameManager().stopGame();
    }

    private void registerCommands() {
        new GameCommand().register();
    }

    private void registerEvents() {
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new GameHandler(), this);
    }

    public static BlockShuffle getInstance() {
        return instance;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public Map<UUID, GamePlayer> getPlayers() {
        return players;
    }

    public GameboardManager getGameboardManager() {
        return gameboardManager;
    }
}
