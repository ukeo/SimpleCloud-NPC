package net.sneakyfire.npc;

import com.github.juliarn.npc.NPCPool;
import net.sneakyfire.npc.commands.Create_Command;
import net.sneakyfire.npc.listener.InventoryClickListener;
import net.sneakyfire.npc.listener.NPCInteractListener;
import net.sneakyfire.npc.util.NPCFileManager;
import net.sneakyfire.npc.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleCloudNPC extends JavaPlugin {

    private static SimpleCloudNPC instance;
    private static NPCPool npcPool;
    private String version;
    private NPCFileManager npcFileManager;

    @Override
    public void onEnable() {
        instance = this;
        npcPool = new NPCPool(this);
        version = this.getDescription().getVersion();
        npcFileManager = new NPCFileManager();
        getLogger().info("Booting SimpleCloudNPC by Espen#0666 version " + version);

        getConfig().options().copyDefaults(true);
        saveConfig();

        npcFileManager.registerNpcInConfig();

        registerCommands();
        registerListener();

    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private void registerCommands() {
        getCommand("cloudnpc").setExecutor(new Create_Command());
    }

    private void registerListener() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new NPCInteractListener(), instance);
        pluginManager.registerEvents(new InventoryClickListener(), instance);
    }

    public static SimpleCloudNPC getInstance() {
        return instance;
    }

    public String getVersion() {
        return version;
    }

    public NPCPool getNpcPool() {
        return npcPool;
    }

    public NPCFileManager getNpcFileManager() {
        return npcFileManager;
    }
}
