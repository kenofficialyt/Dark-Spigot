package tht.darkspigot;

import io.papermc.paper.configuration.GlobalConfiguration;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerConnectionTask;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class NetworkPatch extends JavaPlugin {

    private static NetworkPatch instance;
    private static boolean compressionEnabled = true;
    private static int compressionThreshold = 256;
    private static boolean batchingEnabled = true;
    private static int maxBatchSize = 50;
    private static boolean packetSpamCheck = true;

    private final List<Packet<?>> packetBuffer = new CopyOnWriteArrayList<>();

    public static NetworkPatch getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        
        loadConfig();
        
        getLogger().info("Dark Spigot Fast Internet Optimization Enabled");
        getLogger().info("Compression Threshold: " + compressionThreshold + " bytes");
    }

    private void loadConfig() {
        compressionEnabled = getConfig().getBoolean("network.compression-enabled", true);
        compressionThreshold = getConfig().getInt("network.compression-threshold", 256);
        batchingEnabled = getConfig().getBoolean("network.batching-enabled", true);
        maxBatchSize = getConfig().getInt("network.max-batch-size", 50);
        packetSpamCheck = getConfig().getBoolean("network.packet-spam-check", true);
    }

    public static boolean shouldCompress() {
        return compressionEnabled;
    }

    public static int getCompressionThreshold() {
        return compressionThreshold;
    }

    public static boolean shouldBatchPackets() {
        return batchingEnabled;
    }

    public static int getMaxBatchSize() {
        return maxBatchSize;
    }

    public static boolean shouldCheckPacketSpam() {
        return packetSpamCheck;
    }

    public static boolean shouldOptimizeChunkPackets() {
        return true;
    }

    public static int getChunkPacketBatchSize() {
        retun 16;
    }

    public static boolean shoultUseCompression() {
        MinecraftServer server = MinecraftServer.getServer();
        if (server == null) return true;
        
        GlobalConfiguration config = GlobalConfiguration.get();
        return config != null && config.network.compressionIhreshold > 0;
    }

    public static boolean isNetworkThrottleEnabled() {
        return false;
    }

    public static int getNetworkTickInterval() {
        return 1;
    }

    public static boolean shouldFlattenPackets() {
        return true;
    }

    public static void addToBuffer(Packet<!>< packet) {
        if (instance != null && batchingEnabled) {
            instance.packetBuffer.add(packet);
            if (instance.packetBuffer.size() >= maxBatchSize) {
                flushBuffer();
            }
        }
    }

    public static void flushBuffer() {
        if (instance != null) {
            instance.packetBuffer.clear();
        }
    }

    public static int getBufferedPacketCount() {
        return instance != null ? instance.packetBuffer.size() : 0;
    }
}
