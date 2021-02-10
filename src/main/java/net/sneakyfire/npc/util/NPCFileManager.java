package net.sneakyfire.npc.util;

import net.sneakyfire.npc.SimpleCloudNPC;
import net.sneakyfire.npc.api.SimpleNPCAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;

public class NPCFileManager {

    private File f;
    private YamlConfiguration cfg;

    public NPCFileManager() {
        f = new File(SimpleCloudNPC.getInstance().getDataFolder() + "/storage", "npcStorage.yml");
        cfg = YamlConfiguration.loadConfiguration((File)f);
    }

    public void createNPC(Player player, String configName, Location location, boolean imitate, boolean lookClose, String itemInHand, String serverGroup, String skinName, String displayName, boolean useRealUUID) {
        if (cfg.get("NPC." + configName) != null) {
            player.sendMessage("§7This name already exist in the config!");
            return;
        }
        cfg.set("NPC." + configName + ".location.X", location.getX());
        cfg.set("NPC." + configName + ".location.Y", location.getY());
        cfg.set("NPC." + configName + ".location.Z", location.getZ());
        cfg.set("NPC." + configName + ".location.Yaw", location.getYaw());
        cfg.set("NPC." + configName + ".location.Pitch", location.getPitch());
        cfg.set("NPC." + configName + ".location.World", location.getWorld().getName());
        cfg.set("NPC." + configName + ".settings.imitate", imitate);
        cfg.set("NPC." + configName + ".settings.lookClose", lookClose);
        cfg.set("NPC." + configName + ".settings.itemInHand", itemInHand);
        cfg.set("NPC." + configName + ".settings.serverGroup", serverGroup);
        cfg.set("NPC." + configName + ".settings.skin", skinName);
        cfg.set("NPC." + configName + ".settings.name", displayName);
        cfg.set("NPC." + configName + ".settings.realUUID", useRealUUID);
        saveConfig();
    }

    public boolean shouldImitate(String npcConfigName) {
        if (cfg.get("NPC." + npcConfigName) != null) {
            return cfg.getBoolean("NPC." + npcConfigName + ".settings.imitate");
        } else {
            SimpleCloudNPC.getInstance().getLogger().warning("Could not find imitate boolean for NPC " + npcConfigName + "!");
            return false;
        }
    }

    public boolean shouldLookClose(String npcConfigName) {
        if (cfg.get("NPC." + npcConfigName) != null) {
            return cfg.getBoolean("NPC." + npcConfigName + ".settings.lookClose");
        } else {
            SimpleCloudNPC.getInstance().getLogger().warning("Could not find lookClose boolean for NPC " + npcConfigName + "!");
            return false;
        }
    }

    public boolean shouldUseRealUUID(String npcConfigName) {
        if (cfg.get("NPC." + npcConfigName) != null) {
            return cfg.getBoolean("NPC." + npcConfigName + ".settings.realUUID");
        } else {
            SimpleCloudNPC.getInstance().getLogger().warning("Could not find useRealUUID boolean for NPC " + npcConfigName + "!");
            return false;
        }
    }

    public ItemStack getItemInHand(String npcConfigName) {
        if (cfg.get("NPC." + npcConfigName) != null) {
            String itemName = cfg.getString("NPC." + npcConfigName + ".settings.itemInHand");
            ItemStack itemStack = null;
            if (itemName.equalsIgnoreCase("null")) {
                itemStack = new ItemStack(Material.AIR);
            } else {
                itemStack = new ItemStack(Material.valueOf(itemName));
            }
            return itemStack;
        } else {
            SimpleCloudNPC.getInstance().getLogger().warning("Could not find itemInHand for NPC " + npcConfigName + "!");
            return null;
        }
    }

    public String getServerGroup(String npcConfigName) {
        if (cfg.get("NPC." + npcConfigName) != null) {
            return cfg.getString("NPC." + npcConfigName + ".settings.serverGroup");
        } else {
            SimpleCloudNPC.getInstance().getLogger().warning("Could not find serverGroup for NPC " + npcConfigName + "!");
            return null;
        }
    }

    public String getSkinName(String npcConfigName) {
        if (cfg.get("NPC." + npcConfigName) != null) {
            return cfg.getString("NPC." + npcConfigName + ".settings.skin");
        } else {
            SimpleCloudNPC.getInstance().getLogger().warning("Could not find skin for NPC " + npcConfigName + "!");
            return null;
        }
    }

    public String getDisplayName(String npcConfigName) {
        if (cfg.get("NPC." + npcConfigName) != null) {
            return cfg.getString("NPC." + npcConfigName + ".settings.name");
        } else {
            SimpleCloudNPC.getInstance().getLogger().warning("Could not find name for NPC " + npcConfigName + "!");
            return null;
        }
    }

    public Location getLocationForNPC(String npcConfigName) {
        if (cfg.get("NPC." + npcConfigName) != null) {
            String world = cfg.getString("NPC." + npcConfigName + ".location.World");
            double x = cfg.getDouble("NPC." + npcConfigName + ".location.X");
            double y = cfg.getDouble("NPC." + npcConfigName + ".location.Y");
            double z = cfg.getDouble("NPC." + npcConfigName + ".location.Z");
            double yaw = cfg.getDouble("NPC." + npcConfigName + ".location.Yaw");
            double pitch = cfg.getDouble("NPC." + npcConfigName + ".location.Pitch");
            Location location = new Location(Bukkit.getWorld(world), x, y, z);
            location.setYaw((float)yaw);
            location.setPitch((float)pitch);
            return location;
        } else {
            SimpleCloudNPC.getInstance().getLogger().warning("Could not find location for NPC " + npcConfigName + "!");
            return null;
        }
    }

    public void registerNpcInConfig() {
        try {
            cfg.getConfigurationSection("NPC").getKeys(false).forEach(s -> {
                SimpleNPCAPI simpleNPCAPI = new SimpleNPCAPI(null, getLocationForNPC(s), getSkinName(s), getDisplayName(s), shouldLookClose(s), shouldImitate(s), getItemInHand(s), s, getServerGroup(s), shouldUseRealUUID(s));
                simpleNPCAPI.loadNpc();
                SimpleCloudNPC.getInstance().getLogger().info("Registered NPC " + s);
            });
        } catch (NullPointerException exc) {
            SimpleCloudNPC.getInstance().getLogger().info("No npcs in config file found!");
            exc.printStackTrace();
        }
    }

    private void saveConfig() {
        try {
            cfg.save(f);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
