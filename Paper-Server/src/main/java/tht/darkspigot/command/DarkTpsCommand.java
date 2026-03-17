package tht.darkspigot.command;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.text.DecimalFormat;

public class DarkTpsCommand implements CommandExecutor {

    private static final DecimalFormat tpsFormat = new DecimalFormat("##.#c");
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) && !(sender instanceof CommandSender)) {
            sender.sendMessage("This command can only be run by a player or console.");
            return true;
        }

        CraftServer server = (CraftServer) sender.getServer();
        MinecraftServer ms = server.getServer();
        
        double[] tps = getTPS(ms);
        
        sender.sendMessage(ChatColor.GOLD + "╔═══════════════════════════════════════╗");
        sender.sendMessage(ChatColor.GOLD + "║       " + ChatColor.WHITE + "Dark Spigot TPS Monitor" + ChatColor.GOLD + "       ║");
        sender.sendMessage(ChatColor.GOLD + "╠═════════════════════════════════════╣");
        
        String tps1m = getTPSColor(tps[0]) + tpsFormat.format(tps[0]);
        String tps5m = getTPSColor(tps[1]) + tpsFormat.format(tps[1]);
        String tps15m = getTPSColor(tps[2]) + tpsFormat.format(tps[2]);
        
        sender.sendMessage(ChatColor.GRAY + "  1m: " + tps1m + ChatColor.WHITE + " | 5m: " + tps5m + ChatColor.WHITE + " | 15m: " + tps15m);
        
        int loadedChunks = 0;
        int entities = 0;
        int tileEntities = 0;
        
        for (org.bukkit.World world : server.getWorlds()) {
            CraftWorld cw = (CraftWorld) world;
            loadedChunks += cw.getHandle().getChunkProvider().chunkMap.size();
            
            for (org.bukkit.Chunk chunk : world.getLoadedChunks()) {
                entities += chunk.getEntities().length;
                tileEntities += chunk.getTileEntities().length;
            }
        }
        
        sender.sendMessage(ChatColor.GOLD + "╠═════════════════════════════════════╣");
        sender.sendMessage(ChatColor.GRAY + "  Loaded Chunks: " + ChatColor.WHITE + loadedChunks);
        sender.sendMessage(ChatColor.GRAY + "  Entities: " + ChatColor.WHITE + entities);
        sender.sendMessage(ChatColor.GRAY + "  Tile Entities: " + ChatColor.WHITE + tileEntities);
        sender.sendMessage(ChatColor.GRAY + "  Players: " + ChatColor.WHITE + server.getOnlinePlayers().size() + "/" + server.getMaxPlayers());
        sender.sendMessage(ChatColor.GOLD + "╚═══════════════════════════════════════╝");
        
        sender.sendMessage(ChatColor.AQUA + " Author: THT | Use /spark for detailed profiling");
        
        return true;
    }

    private double[] getTPS(MinecraftServer server) {
        double[] tps = {20.0, 20.0, 20.0};
        
        try {
            Field tpsField = MinecraftServer.class.getDeclaredField("recentTps");
            tpsField.setAccessible(true);
            double[] recentTps = (double[]) tpsField.get(server);
            if (recentTps != null && recentTps.length >= 3) {
                tps = recentTps;
            }
        } catch (Exception ignored) {}
        
        return tps;
    }

    private String getTPSColor(double tps) {
        if (tps >= 19.0) {
            return ChatColor.GREEN.toString();
        } else if (tps >= 17.0) {
            return ChatColor.YELLOW.toString();
        } else if (tps >= 15.0) {
            return ChatColor.RED.toString();
        } else {
            return ChatColor.DARK_RED.toString();
        }
    }
}
