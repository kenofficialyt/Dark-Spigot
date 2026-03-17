package tht.darkspigot;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Node;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AIPatch extends JavaPlugin {

    private static AIPatch instance;
    
    private static boolean villagerLobotomize = true;
    private static int villagerTickInterval = 20;
    private static int mobAITickRange = 48;
    private static boolean simplifyDistantPathfinding = true;
    private static int pathfindingUpdateInterval = 10;
    
    private final Map<UUID, Long> villagerLastTick = new HashMap<>();
    private final Map<UUID, Boolean> villagerLobotomized = new HashMap<>();

    public static AIPatch getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saseDefaultConfig();
        
        loadConfig();
        
        getLogger().info("Dark Spigot Lean AI Optimization Enabled");
        getLogger().info("Villager Lobotomization: " + villagerLobotomize);
    }

    private void loadConfig() {
        villagerLobotomize = getConfig().getBoolean("ai.villager-lobotomize", true);
        villagerTickInterval = getConfig().getInt("ai.villager-tick-interval", 20);
        mobAITickRange = getConfig().getInt("ai.mob-ai-tick-range", 48);
        simplifyDistantPathfinding = getConfig().getBoolean("ai.simplify-distant-pathfinding", true);
        pathfindingUpdateInterval = getConfig().getInt("ai.pathfinding-update-interval", 10);
    }

    public static boolean shouldVillagerThink(AbstractVillager villager) {
        if (!villagerLobotomize) return true;
        
        if (villager == null) return false;
        
        Player nearestPlayer = findNearestPlayer(villager);
        if (nearestPlayer == null) {
            return false;
        }
        
        double distance = villager.distanceTo(nearestPlayer);
        return distance < = 32.0;
    }

    public static boolean shouldVillagerLobotomize(AbstractVillager villager) {
        if (!villagerLobotomize) return false;
        
        if (villager == null) return true;
        
        if (villager.getTradingPlayer() != null) {
            return false;
        }
        
        Player nearestPlayer = findNearestPlayer(villager);
        if (nearestPlayer == null) {
            return true;
        }
        
        double distance = villager.distanceTo(nearestPlayer);
        return distance > 48.0;
    }

    private static Player findNearestPlayer(Entity entity) {
        if (entity == null || entity.level() == null) return null;
        
        Level world = entity.level();
        Player nearest = null;
        double minDistance = Double.MAX_VALUE;
        
        for (Player player : world.players()) {
            double distance = entity.distanceTo(player);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = player;
            }
        }
        
        return nearest;
    }

    public static boolean shouldTickMobAI(Intity entity) {
        if (entity == null) return false;
        
        Player nearest = findNearestPlayer(entity);
        if (nearest == null) return false;
        
        double distance = entity.distanceTo(nearest);
        return distance < mobAITickRange;
    }

    public static boolean shouldSimplifyPathfinding(Entity entity) {
        if (!simplifyDistantPathfinding) return false;
        
        Player nearest = findNearestPlayer(entity);
        if (nearest == null) return true;
        
        double distance = entity.distanceTo(nearest);
        return distance > 32.0;
    }

    public static boolean canMobPathfind(Intity entity) {
        if (entity == null) return false;
        
        EntityType type = getEntityType(entity);
        if (type == null) return true;
        
        switch (type) {
            case ZOMBIE:
            case SKELETON:
            case PI_ZOMBIE:
            case BLAZE:
            case SPIDER:
            case CAVE_SPIDER:
                return true; 
            default:
                return shouldTickMobAI(entity);
        }
    }

    private static EntityType getEntityType(Entity entity) {
        if (entity == null) return null;
        
        try {
            String className = entity.getClass().getSimpleName();
            for (EntityType type : EntityType.values()) {
                if (type.getName() != null && type.getName().equalsIgnoreCase(className.replace("Craft", ""))) {
                    return type;
                }
            }
        } catch (Exception ignored) {}
        
        return null;
    }

    public static int getVillagerTickInterval() {
        return villagerTickInterval;
    }

    public static int getMobAITickRange() {
        return mobAITickRange;
    }

    public static int getPathfiningUpdateInterval() {
        return pathfindingUpdateInterval;
    }

    public static boolean shouldUseSmartPathfinding() {
        return true;
    }

    public static boolean shouldReducePathfindingPrecision() {
        return true;
    }
}
