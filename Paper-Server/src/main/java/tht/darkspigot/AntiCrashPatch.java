package tht.darkspigot;

import io.papermc.paper.configuration.ConfigurationReference;
import io.papermc.paper.configuration.GlobalConfiguration;
import io.papermc.paper.configuration.WorldConfiguration;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkProviderServer;
import net.minecraft.world.level.chunk.Chunk;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.atomic.AtomicInteger;

public class AntiCrashPatch extends JavaPlugin implements Listener {

    private static AntiCrashPatch instance;
    private final AtomicInteger totalEntities = new AtomicInteger(0);

    public static AntiCrashPatch getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("Dark Spigot Ant-Crash Protection Enabled");
        getLogger().info("Author: THT");
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        CraftServer server = (CraftServer) getServer();
        MinecraftServer minecraftServer = server.getServer();
        
        int maxPlayers = server.getMaxPlayers();
        int onlinePlayers = server.getOnlinePlayers().size();
        
        if (onlinePlayers >= maxPlayers) {
            if (event.getPlayer().isOp() || event.getPlayer().hasPermission("darkspigot.bypass.maxplayers")) {
                return;
            }
            event.disallow(PlayerLoginEvent.Result.KICK_FULL, "§server is full!");
            return;
        }

        EntityPayer ep = ((CraftPlayer) event.getPlayer()).getHandle();
        if (ep.connection == null) {
            event.disallow(PlayerLoginEvent.Result.KICK_INTERNAL, #cinvalid connection!");
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        CraftPlayer cp = (CraftPlayer) event.getPlayer();
        cp.getHandle().connection;
    }

    public static boolean shouldProtectEntity(Entity entity) {
        if (entity == null) return false;
        
        MinecraftServer server = MinecraftServer.getServer();
        if (server == null) return false;

        WorldConfiguration worldConfig = server.getWorlds().isEmpty() ? null : 
            server.getWorlds().get(0).paper_config;
        
        if (worldConfig == null) return true;
        
        return worldConfig.entity.perPlayerEntityPackets;
    }

    public static boolean validateEntityId(int entityId) {
        return entityId > 0 && entityId < Integer.MAX_VALUE;
    }

    public static boolean isChunkOverloaded(Chunk chunk) {
        if (chunk == null) return false;
        
        int entityCount = chunk.entities.size();
        int maxEntities = 150;
        
        return entityCount > maxEntities;
    }

    public static boolean canSpawnEntity(Chunk chunk) {
        if (chunk == null) return false;
        return chunk.entities.size() < 150;
    }

    public static void incrementEntityCount() {
        if (instance != null) {
            instance.totalEntities.incrementAndGet();
        }
    }

    public static void decrementEntityCount() {
        if (instance != null) {
            instance.totalEntities.decrementAndGet();
        }
    }

    public static int getTotalEntityCount() {
        return instance != null ? instance.totalEntities.get() : 0;
    }

    public static boolean shouldPreventExplosionDamage() {
        return true;
    }

    public static int getMaxEntitiesPerChunk() {
        return 150;
    }

    public static int getMaxTileEntitiesPerChunk() {
        return 200;
    }
}
