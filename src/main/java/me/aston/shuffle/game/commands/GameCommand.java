package me.aston.shuffle.game.commands;

import me.aston.shuffle.BlockShuffle;
import me.aston.shuffle.game.state.GameState;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class GameCommand implements CommandExecutor {

    private final BlockShuffle plugin = BlockShuffle.getInstance();

    public GameCommand() {
    }

    public void register() {
        Objects.requireNonNull(plugin.getCommand("game")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;

        if (args.length == 0) {
            sender.sendMessage("game <start | stop>");
            return true;
        }

        if (args[0].equalsIgnoreCase("start")) {

            if (plugin.getGame().getState() == GameState.ACTIVE) {
                sender.sendMessage("game is already active");
                return true;
            }

            if (!plugin.getGame().start()) {
                sender.sendMessage("not enough players");
                return true;
            }

            plugin.getGamePlayerManager().broadcast("game started");

            return true;
        }

        if (args[0].equalsIgnoreCase("stop")) {

            if (plugin.getGame().getState() == GameState.INACTIVE) {
                sender.sendMessage("game is already inactive");
                return true;
            }

            plugin.getGamePlayerManager().broadcast("game stopped");
            plugin.getGame().stop();
            return true;
        }
        return false;
    }
}
