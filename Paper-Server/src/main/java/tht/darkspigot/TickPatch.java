package tht.darkspigot;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public class TickPatch extends JavaPlugin {

    private static TickPatch instance;
    private static double tickRate = 20.0;
    private static int entityActivationRange = 32;
    private static int animalActivationRange = 32;
    private static int monsterActivationRange = 48;
    private static int miscActivationRange = 16;

    public static TickPatch getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        
        loadConfig();
        
        getLogger().info("Dark Spigot Tick Optimization Enabled");
        getLogger().info("Target Tick Rate: " + tickRate + " TPS");
    }

    private void loadConfig() {
        entityActivationRange = getConfig().getInt("tick-optimizer.entity-activation-range", 32);
        animalActivationRange = getConfig().getInt("tick-optimizer.animal-activation-range", 32);
        monsterActivationRange = getConfig().getInt("tick-optimizer.monster-activation-range", 48);
        miscActivationRange = getConfig().getInt("tick-optimizer.misc-activation-range", 16);
    }

    public static boolean shouldTickEntity(Entity entity, double distanceSq) {
        if (entity == null) return false;
        
        int activationRange;
        
        EntityType type = EntityType.fromName(org.bukkit.craftbukkit.util.CraftMagicNumbers.getEntityType(entity.getClass()));
        if (type == null) {
            activationRange = miscActivationRange;
        } else {
            switch (type) {
                case ZOMBIE:
                case SKELETON:
                case CREEPER:
                case SPIDER:
                case BLAZE:
                case ENDERMAN:
                case PIG_ZOMBIE:
                case WITCH:
                case HOGLIN:
                case ZOGLIN:
                case PIGLIN:
                case PILLAGER:
                case RAVAGER:
                case VEX:
                case ILLUSIONER:
                case SHULKER:
                    activationRange = monsterActivationRange;
                    break;
                case COW:
                case PIG:
                case SHEEP:
                case CHICKEN:
                case HORSE:
                case DONKEY:
                case MULE:
                case LLAMA:
                case RABBIT:
                case TURTLE:
                case PANDA:
                case FOX:
                case BEE:
                case OCELOT:
                case WOLF:
                case CAT:
                case PARROT:
                case SNIFFER:
                    activationRange = animalActivationRange;
                    break;
                default:
                    activationRange = miscActivationRange;
                    break;
            }
        }
        
        double rangeSq = (double) (activationRange * activationRange);
        return distanceSq < rangeSq;
    }

    public static int getEntityActivationRange() {
        return entityActivationRange;
    }

    public static int getAnimalActivationRange() {
        return animalActivationRange;
    }

    public static int getMonsterActivationRange() {
        return monsterActivationRange;
    }

    public static int getMiscActivationRange() {
        return miscActivationRange;
    }

    public static boolean isAsyncChunkLoading() {
        return true;
    }

    public static int getTickSpoolMax() {
        return 3;
    }

    public static boolean shouldIgnoreEntityTick(Entity entity) {
        if (entity == null) return false;
        return !entity.isAlive();
    }

    public static boolean shouldUseFastTileEntityTick() {
        return true;
    }

    public static long getMaxTickTime() {
        return 50L;
    }
}
