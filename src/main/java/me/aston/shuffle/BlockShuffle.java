package me.aston.shuffle;

import me.aston.shuffle.game.GameCommand;
import me.aston.shuffle.game.*;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlockShuffle extends JavaPlugin {

    private static BlockShuffle instance;
    private Game game;
    private Gamebaord gamebaord;
    private GameManager gameManager;

    @Override
    public void onEnable() {
        instance = this;
        game = new Game();
        gamebaord = new Gamebaord();
        gameManager = new GameManager();

        registerEvents();
        registerCommands();
    }

    @Override
    public void onDisable() {
        gameManager.stopGame();
    }

    private void registerCommands() {
        new GameCommand().register();
    }

    private void registerEvents() {
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new GameListener(), this);
    }

    public static BlockShuffle getInstance() {
        return instance;
    }

    public Game getGame() {
        return this.game;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public Gamebaord getGameboard() {
        return gamebaord;
    }
}
