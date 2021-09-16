package eu.imposdev.npc;

import com.github.juliarn.npc.NPCPool;
import eu.imposdev.npc.command.CreateCommand;
import eu.imposdev.npc.listener.InventoryClickListener;
import eu.imposdev.npc.listener.PlayerNPCInteractListener;
import eu.imposdev.npc.npc.util.NPCCreateUtil;
import eu.imposdev.npc.npc.util.NPCFileManager;
import eu.imposdev.npc.npc.util.NPCUtil;
import eu.imposdev.npc.util.ServerVersion;
import eu.imposdev.npc.util.UpdateChecker;
import eu.imposdev.npc.util.Utils;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;

public final class SimpleCloudNPC extends JavaPlugin {

    private static SimpleCloudNPC instance;

    private NPCPool npcPool;
    private NPCUtil npcUtil;
    private NPCFileManager npcFileManager;
    private NPCCreateUtil npcCreateUtil;

    private String version;

    private Metrics metrics;

    private ServerVersion serverVersion;

    private CommandMap commandMap;

    private UpdateChecker updateChecker;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        loadLibraries();
        loadConfig();
        loadNpcPool();
        registerListener();
        loadCommandMap();
        registerCommand(new CreateCommand("cloudnpc"));
    }

    @Override
    public void onDisable() {

    }

    protected void loadConfig() {
        getConfig().options().copyDefaults(true);
        getConfig().options().header("SimpleCloudNPC Plugin by Espen | Version 2.0.0 | Copyright (c) 2021 Espen da Silva");
        getConfig().addDefault("Settings.SpawnDistance", 50);
        getConfig().addDefault("Settings.ActionDistance", 50);
        getConfig().addDefault("Settings.TabListRemoveTicks", 20);

        ArrayList<String> list = new ArrayList<>();
        list.add("§7Players§8: §a%online_players%§7/§c%max_players%");
        list.add("§7Map§8: §a%motd%");
        list.add("§6This service is full!");

        getConfig().addDefault("Inventory.ServerFull.name", "§a%server%");
        getConfig().addDefault("Inventory.ServerFull.material", "STAINED_CLAY");
        getConfig().addDefault("Inventory.ServerFull.subId", 1);
        getConfig().addDefault("Inventory.ServerFull.lore", list);
        getConfig().addDefault("Inventory.ServerEmpty.name", "§a%server%");
        getConfig().addDefault("Inventory.ServerEmpty.material", "STAINED_CLAY");
        getConfig().addDefault("Inventory.ServerEmpty.subId", 5);
        getConfig().addDefault("Inventory.ServerEmpty.lore", list);

        saveConfig();
    }

    protected void loadNpcPool() {
        npcPool = NPCPool.builder(this)
                .spawnDistance(getConfig().getInt("Settings.SpawnDistance"))
                .actionDistance(getConfig().getInt("Settings.ActionDistance"))
                .tabListRemoveTicks(getConfig().getInt("Settings.TabListRemoveTicks"))
                .build();
    }

    protected void loadLibraries() {
        this.version = this.getDescription().getVersion();

        updateChecker = new UpdateChecker();
        updateChecker.check();
        if (updateChecker.isAvailable()) {
            getLogger().warning("There is an update available! Gop and check out the github page! https://github.com/ukeo/SimpleCloud-NPC");
        } else {
            getLogger().info("You are running the latest version of SimpleCloud-NPC!");
        }

        for (ServerVersion version : ServerVersion.values()) {
            if (Utils.getServerVersion().startsWith(version.name())) {
                serverVersion = version;
                getLogger().info("Detected server version " + version.name());
            }
        }
        npcUtil = new NPCUtil();
        metrics = new Metrics(this, 10287);
        npcFileManager = new NPCFileManager();
        npcCreateUtil = new NPCCreateUtil();
    }

    protected void registerListener() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new InventoryClickListener(), instance);
        pluginManager.registerEvents(new PlayerNPCInteractListener(), instance);
    }

    protected void loadCommandMap() {
        final Field bukkitCommandMap;
        try {
            bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            getLogger().info("Failed init of CommandMap:");
            exception.printStackTrace();
        }
    }

    protected void registerCommand( Command command ) {
        commandMap.register( command.getName(), command );
    }

    public static SimpleCloudNPC getInstance() {
        return instance;
    }

    public NPCPool getNpcPool() {
        return npcPool;
    }

    public NPCUtil getNpcUtil() {
        return npcUtil;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public NPCFileManager getNpcFileManager() {
        return npcFileManager;
    }

    public NPCCreateUtil getNpcCreateUtil() {
        return npcCreateUtil;
    }

    public ServerVersion getServerVersion() {
        return serverVersion;
    }

    public String getVersion() {
        return version;
    }

    public CommandMap getCommandMap() {
        return commandMap;
    }
}
