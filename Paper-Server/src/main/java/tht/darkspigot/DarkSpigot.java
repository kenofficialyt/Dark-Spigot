package tht.darkspigot;

import net.minecraft.server.MinecraftServer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import tht.darkspigot.command.DarkTpsCommand;

import java.util.Arrays;

public class DarkSpigot extends JavaPlugin {

    private static DarkSpigot instance;
    private AntiCrashPatch antiCrashPatch;
    private TickPatch tickPatch;
    private NetworkPatch networkPatch;
    private AIPatch aiPatch;
    private ChunkPatch chunkPatch;

    public static DarkSpigot getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
        getLogger().info("Dark Spigot v1.0 by THT is loading...");
    }

    @Override
    public void onEnable() {
        getLogger().info(ChatColor.GOLD + "╔═══════════════════════════════════════════════════════╗");
        getLogger().info(ChatColor.GOLD + "║         DARK SPIGOT v1.0              ║");
        getLogger().info(ChatColor.GOLD + "║         Author: THT                   ║");
        getLogger().info(ChatColor.GOLD + "╚═══════════════════════════════════════╝");
        
        initializePatches();
        
        registerCommands();
        
        getLogger().info(ChatColor.GREEN + "Dark Spigot enabled successfully!");
        getLogger().info(ChatColor.AQUA + "Optimizations: Tick, Network, AI, Chunk, Anti-Crash");
        getLogger().info(ChatColor.AQUA + "Real-time monitoring: Spark integrated");
    }

    @Override
    public void onDisable() {
        getLogger().info("Dark Spigot v1.0 is shutting down...");
    }

    private void initializePatches() {
        try {
            antiCrashPatch = new AntiCrashPatch();
            antiCrashPatch.onEnable();
            
            tickPatch = new TickPatch();
            tickPatch.onEnable();
            
            networkPatch = new NetworkPatch();
            networkPatch.onEnable();
            
            aiPatch = new AIPatch();
            aiPatch.onEnable();
            
            chunkPatch = new ChunkPatch();
            chunkPatch.onEnable();
            
            getLogger().info("All patches initialized successfully");
        } catch (Exception e) {
            getLogger().severe("Failed to initialize patches: " + e.getMessage());
        }
    }

    private void registerCommands() {
        DarkTpsCommand darkTpsCommand = new DarkTpsCommand();
        getCommand("darktps").setExecutor(darkTpsCommand);
        getCommand("darkspigot").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("darkspigot")) {
            if (!sender.hasPermission("darkspigot.admin")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }
            
            if (args.length == 0) {
                sender.sendMessage(ChatColor.GOLD + "=== Dark Spigot v1.0 by THT ===");
                sender.sendMessage(ChatColor.AQUA + "/darktps - View server TPS");
                sender.sendMessage(ChatColor.AQUA + "/darkspigot reload - Reload config");
                return true;
            }
            
            if (args[0].equalsIgnoreCase("reload")) {
                reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "Dark Spigot config reloaded!");
                return true;
            }
            
            sender.sendMessage(ChatColor.RED + "Unknown command. Use /darkspigot for help.");
            return true;
        }
        
        return false;
    }

    public AntiCrashPatch getAntiCrashPatch() {
        return antiCrashPatch;
    }

    public TickPatch getTickPatch() {
        return tickPatch;
    }

    public NetworkPatch getNetworkPatch() {
        return networkPatch;
    }

    public AIPatch getAiPatch() {
        return aiPatch;
    }

    public ChunkPatch getChunkPatch() {
        return chunkPatch;
    }
}
