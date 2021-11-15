package me.aston.bs;

import me.aston.bs.commands.GameCommand;
import me.aston.bs.modules.BSHandler;
import me.aston.bs.modules.BSManager;
import me.aston.bs.modules.BSPlayer;
import me.aston.bs.modules.SBManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class BS extends JavaPlugin {

    private static BS instance;
    private Map<UUID, BSPlayer> players;
    private BSManager bsManager;
    private SBManager sbManager;

    @Override
    public void onEnable() {
        instance = this;
        players = new HashMap<>();
        bsManager = new BSManager();
        sbManager = new SBManager();

        new GameCommand();
        getServer().getPluginManager().registerEvents(new BSHandler(), this);
    }

    @Override
    public void onDisable() {
        getBsManager().stopGame();
    }

    public static BS getInstance() {
        return instance;
    }

    public BSManager getBsManager() {
        return bsManager;
    }

    public Map<UUID, BSPlayer> getPlayers() {
        return players;
    }

    public SBManager getSbManager() {
        return sbManager;
    }
}
