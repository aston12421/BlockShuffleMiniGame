package me.aston.shuffle.game;

import me.aston.shuffle.BlockShuffle;
import me.aston.shuffle.game.GameState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class GameCommand implements CommandExecutor {

    private final BlockShuffle plugin = BlockShuffle.getInstance();
    private final Game game = plugin.getGame();

    public void register() {
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
            if (game.getGameState() == GameState.ACTIVE) {
                sender.sendMessage("the game is already active!");
                return true;
            }
            plugin.getGameManager().startGame();
            return true;
        }

        if (args[0].equalsIgnoreCase("stop")) {
            if (game.getGameState() == GameState.INACTIVE) {
                sender.sendMessage("the game has not yet started");
                return true;
            }
            plugin.getGameManager().stopGame();
        }

        return false;
    }
}
