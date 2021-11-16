package me.aston.blockshuffle.modules;

import me.aston.blockshuffle.BlockShuffle;
import me.aston.blockshuffle.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.*;

public final class GameboardManager {

    private final BlockShuffle plugin = BlockShuffle.getInstance();

    public void setGameScoreboard(GamePlayer gamePlayer) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("BlockShuffle", "dummy");

        obj.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "BLOCKSHUFFLE");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        int mins = plugin.getGameManager().mins;
        int secs = plugin.getGameManager().mins;

        Team block = board.registerNewTeam("block");
        block.addEntry(ChatColor.RED.toString());
        block.setPrefix("Block: ");
        block.setSuffix(ChatColor.BLUE + gamePlayer.getMaterial().name().replace("_", " "));
        Team time = board.registerNewTeam("round_timer");
        time.addEntry(ChatColor.GREEN.toString());
        time.setPrefix("Time: ");
        time.setSuffix(ChatColor.GREEN + Utils.twoDigitString(mins) + ":" + Utils.twoDigitString(secs));
        obj.getScore(ChatColor.GREEN.toString()).setScore(1);
        obj.getScore(ChatColor.RED.toString()).setScore(2);

        Bukkit.getPlayer(gamePlayer.getUuid()).setScoreboard(board);

    }

}
