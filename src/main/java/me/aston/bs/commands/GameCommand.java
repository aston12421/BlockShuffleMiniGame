package me.aston.bs.commands;

import me.aston.bs.BS;
import me.aston.bs.modules.GameStates;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GameCommand implements CommandExecutor {

    private final BS plugin = BS.getInstance();

    public GameCommand() {
        plugin.getCommand("game").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;

        if (args.length == 0) {
            sender.sendMessage("/game <start | stop>");
            return true;
        }

        if (args[0].equalsIgnoreCase("start")) {
            if (plugin.getBsManager().getState() == GameStates.ACTIVE) {
                sender.sendMessage("the game is already active!");
                return true;
            }
            plugin.getBsManager().startGame();
            return true;
        }

        if (args[0].equalsIgnoreCase("stop")) {
            if (plugin.getBsManager().getState() == GameStates.INACTIVE) {
                sender.sendMessage("the game has not yet started");
                return true;
            }
            plugin.getBsManager().stopGame();
        }

        return false;
    }
}
