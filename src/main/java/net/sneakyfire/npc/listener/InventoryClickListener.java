package net.sneakyfire.npc.listener;

import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.player.ICloudPlayer;
import eu.thesimplecloud.api.service.ICloudService;
import eu.thesimplecloud.clientserverapi.lib.promise.ICommunicationPromise;
import net.sneakyfire.npc.SimpleCloudNPC;
import net.sneakyfire.npc.util.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null) return;
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            Utils.NPC_CACHE.forEach((npc, s) -> {
                if (event.getClickedInventory().getTitle().equalsIgnoreCase(SimpleCloudNPC.getInstance().getNpcFileManager().getDisplayName(s))) {
                    event.setCancelled(true);
                    String serverGroup = SimpleCloudNPC.getInstance().getNpcFileManager().getServerGroup(s);
                    if (event.getCurrentItem().getItemMeta().getDisplayName().contains(serverGroup)) {
                        String colorFull = SimpleCloudNPC.getInstance().getConfig().getString("Inventory.ServerFull.name").replaceAll("%server%", "");
                        String colorEmpty = SimpleCloudNPC.getInstance().getConfig().getString("Inventory.ServerEmpty.name").replaceAll("%server%", "");
                        String server = event.getCurrentItem().getItemMeta().getDisplayName().replaceAll(colorFull, "").replaceAll(colorEmpty, "");
                        sendPlayer(player.getUniqueId(), server);
                        player.closeInventory();
                    }
                }
            });
        }
    }

    public void sendPlayer(UUID uuid, String server) {
        ICloudService cloudService = CloudAPI.getInstance().getCloudServiceManager().getCloudServiceByName(server);
        ICommunicationPromise<ICloudPlayer> promise = CloudAPI.getInstance().getCloudPlayerManager().getCloudPlayer(uuid);
        if (cloudService.isOnline()) {
            promise.then((iCloudPlayer) -> {
                return iCloudPlayer.connect(cloudService);
            });
        }

    }

}
