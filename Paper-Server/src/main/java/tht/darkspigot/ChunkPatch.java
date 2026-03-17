package tht.darkspigot;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ChunkProviderServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.CompletableFuture;

public class ChunkPatch extends JavaPlugin {

    private static ChunkPatch instance;
    
    private static double playerChunkLoadRate = 100.0;
    private static int maxConcurrentChunkLoads = 4;
    private static int chunkGCPeriod = 400;
    private static boolean asyncChunkLoading = true;
    private static int chunkPriorityDistance = 2;
    private static boolean preventMovingIntoUnloaded = true;

    public static ChunkPatch getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        
        loadConfig();
        
        getLogger().info("Dark Spigot Smooth Chunk Load Optimization Enabled");
        getLogger().info("Async Chunk Loading: " + asyncChunkLoading);
    }

    private void loadConfig() {
        playerChunkLoadRate = getConfig().getDouble("chunk.player-chunk-load-rate", 100.0);
        maxConcurrentChunkLoads = getConfig().getInt("chunk.max-concurrent-chunk-loads", 4);
        chunkGCPeriod = getConfig().getInt("chunk.gc-period", 400);
        asyncChunkLoading = getConfig().getBoolean("chunk.async-loading", true);
        chunkPriorityDistance = getConfig().getInt("chunk.priority-distance", 2);
        preventMovingIntoUnloaded = getConfig().getBoolean("chunk.prevent-moving-into-unloaded", true);
    }

    public static double getPlayerChunkLoadRate() {
        return playerChunkLoadRate;
    }

    public static int getMaxConcurrentChunkLoads() {
        return maxConcurrentChunkLoads;
    }

    public static int getChunkGCPeriod() {
        return chunkGCPeriod;
    }

    public static boolean isAsyncChunkLoadingEnabled() {
        return asyncChunkLoading;
    }

    public static int getChunkPriorityDistance() {
        return chunkPriorityDistance;
    }

    public static boolean shouldPreventMovingIntoUnloaded() {
        return preventMovingIntoUnloaded;
    }

    public static boolean shouldPreloadChunks() {
        return true;
    }

    public static boolean shouldUseSmoothChunkLoading() {
        return true;
    }

    public static int getChunkBatchSize() {
        return 4;
    }

    public static boolean shouldOptimizeChunkSelection() {
        return true;
    }

    public static CompletableFuture<ChunkAccess> getChunkAtAsync(ServerLevel world, int x, int z, ChunkStatus status, boolean exactStatus) {
        ChunkProviderServer chunkProvider = world.getChunkProvider();
        
        if (chunkProvider == null) {
            return CompletableFuture.completedFuture(null);
        }
        
        if (asyncChunkLoading) {
            return chunkProvider.getChunkAtAsynchronously(x, z, status, exactStatus);
        } else {
            ChunkAccess chunk = chunkProvider.getChunk(x, z, status, exactStatus);
            return CompletableFuture.completedFuture(chunk);
        }
    }

    public static boolean shouldLoadChunk(int chunkX, int chunkZ, int playerChunkX, int playerChunkZ) {
        int distance = Math.max(Math.abs(chunkX - playerChunkX), Math.abs(chunkZ - playerChunkZ));
        
        int viewDistance = MinecraftServer.getServer() != null ? 
            MinecraftServer.getServer().getPlayerList().getViewDistance() : 8;
        
        return distance <= viewDistance + chunkPriorityDistance;
    }

    public static int getChunkLoadPriority(int chunkX, int chunkZ, int playerChunkX, int playerChunkZ) {
        int distance = Math.max(Math.abs(chunkX - playerChunkX), Math.abs(chunkZ - playerChunkZ));
        
        if (distance <= chunkPriorityDistance) {
            return 0;
        } else if (distance <= chunkPriorityDistance * 2) {
            return 1;
        } else {
            return 2;
        }
    }

    public static boolean shouldTickChunk(ChunkAccess chunk) {
        if (chunk == null) return false;
        
        return chunk.getInhabitedTime() < Long.MAX_VALUE;
    }

    public static long getChunkUnloadDelay() {
        return 300L;
    }

    public static boolean shouldUsePacketChunkLoading() {
        return true;
    }
}
