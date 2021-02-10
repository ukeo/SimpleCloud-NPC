package net.sneakyfire.npc.listener;

import com.github.juliarn.npc.NPC;
import com.github.juliarn.npc.event.PlayerNPCInteractEvent;
import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.service.ICloudService;
import eu.thesimplecloud.api.service.ServiceState;
import net.sneakyfire.npc.SimpleCloudNPC;
import net.sneakyfire.npc.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class NPCInteractListener implements Listener {

    @EventHandler
    public void onNPCInteract(PlayerNPCInteractEvent event) {
        Player player = event.getPlayer();
        NPC eventNpc = event.getNPC();
        Utils.NPC_CACHE.forEach((npc, s) -> {
            if (eventNpc.getEntityId() == npc.getEntityId()) {
                Inventory inventory = Bukkit.createInventory(null, 9 * 6, SimpleCloudNPC.getInstance().getNpcFileManager().getDisplayName(s));
                ArrayList<ICloudService> list = new ArrayList<>();
                CloudAPI.getInstance().getCloudServiceGroupManager().getServiceGroupByName(SimpleCloudNPC.getInstance().getNpcFileManager().getServerGroup(s)).getAllServices().forEach(iCloudService -> {
                    if (iCloudService.getState().equals(ServiceState.VISIBLE)) {
                        list.add(iCloudService);
                    }
                });
                for (int i = 0; i < 9; i++) {
                    inventory.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7));
                }
                for (int i = 9; i < list.size() + 8; i++) {
                    int finalI = i;
                    list.forEach(iCloudService -> {
                        ItemStack server = null;
                        if (iCloudService.getMaxPlayers() == iCloudService.getOnlineCount()) {
                            server = Utils.getServerItem("ServerFull", iCloudService.getName(), iCloudService.getOnlineCount(), iCloudService.getMaxPlayers(), iCloudService.getMOTD());
                        } else {
                            server = Utils.getServerItem("ServerEmpty", iCloudService.getName(), iCloudService.getOnlineCount(), iCloudService.getMaxPlayers(), iCloudService.getMOTD());
                        }
                        inventory.setItem(finalI, server);
                    });
                }
                player.openInventory(inventory);
            }
        });
    }

}
