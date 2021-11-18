package me.aston.shuffle;

import me.aston.shuffle.game.Game;
import me.aston.shuffle.game.commands.GameCommand;
import me.aston.shuffle.game.listeners.GameListener;
import me.aston.shuffle.game.player.GamePlayerManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlockShuffle extends JavaPlugin {

    private static BlockShuffle instance;
    private GamePlayerManager gamePlayerManager;
    private Game game;

    public static BlockShuffle getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        gamePlayerManager = new GamePlayerManager();
        game = new Game();

        registerEvents();
        registerCommands();
    }

    @Override
    public void onDisable() {
        getGame().stop();
    }

    private void registerCommands() {
        new GameCommand().register();
    }

    private void registerEvents() {
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new GameListener(), this);
    }

    public GamePlayerManager getGamePlayerManager() {
        return gamePlayerManager;
    }

    public Game getGame() {
        return game;
    }

}
