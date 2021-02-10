package net.sneakyfire.npc.util;

import com.github.juliarn.npc.NPC;
import net.sneakyfire.npc.SimpleCloudNPC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Utils {

    public static final HashMap<NPC, String> NPC_CACHE = new HashMap<>();

    public static void reloadNpc(Player player) {
        try {
            if (!NPC_CACHE.isEmpty()) {
                NPC_CACHE.forEach((npc, s) -> {
                    SimpleCloudNPC.getInstance().getNpcPool().removeNPC(npc.getEntityId());
                });
                NPC_CACHE.clear();
                Bukkit.getScheduler().scheduleSyncDelayedTask(SimpleCloudNPC.getInstance(), () -> {
                    player.sendMessage("§aNPC reload complete.");
                    SimpleCloudNPC.getInstance().getNpcFileManager().registerNpcInConfig();
                }, 25L);
            } else {
                player.sendMessage("§cCannot find any npc in current cache to reload!");
                SimpleCloudNPC.getInstance().getLogger().warning("Cannot find any cached npc!");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            player.sendMessage("§cAn error occurred while trying to reload npc. See console log for more information.");
        }
    }

    public static ItemStack getServerItem(String configValue, String serverName, int playerAmount, int maxAmount, String motd) {
        String itemName = SimpleCloudNPC.getInstance().getConfig().getString("Inventory." + configValue + ".material");
        String displayName = SimpleCloudNPC.getInstance().getConfig().getString("Inventory." + configValue + ".name").replaceAll("%server%", serverName);
        int subId = SimpleCloudNPC.getInstance().getConfig().getInt("Inventory." + configValue + ".subId");
        List<String> lore = SimpleCloudNPC.getInstance().getConfig().getStringList("Inventory." + configValue + ".lore");
        List<String> finalLore = new ArrayList<>();
        lore.forEach(s -> {
            s = s.replaceAll("%online_players%", String.valueOf(playerAmount)).replaceAll("%max_players%", String.valueOf(maxAmount)).replaceAll("%motd%", motd);
            finalLore.add(s);
        });
        ItemStack itemStack = new ItemStack(Material.valueOf(itemName), playerAmount, (byte) subId);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(finalLore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
