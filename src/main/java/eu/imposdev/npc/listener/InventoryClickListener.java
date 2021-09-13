package eu.imposdev.npc.listener;

import eu.imposdev.npc.SimpleCloudNPC;
import eu.imposdev.npc.util.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null) return;
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            Utils.NPC_CACHE.forEach((npc, s) -> {
                if (event.getView().getTitle().equalsIgnoreCase(SimpleCloudNPC.getInstance().getNpcFileManager().getDisplayName(s))) {
                    event.setCancelled(true);
                    String serverGroup = SimpleCloudNPC.getInstance().getNpcFileManager().getServerGroup(s);
                    if (event.getCurrentItem().getItemMeta().getDisplayName().contains(serverGroup)) {
                        String colorFull = SimpleCloudNPC.getInstance().getConfig().getString("Inventory.ServerFull.name").replaceAll("%server%", "");
                        String colorEmpty = SimpleCloudNPC.getInstance().getConfig().getString("Inventory.ServerEmpty.name").replaceAll("%server%", "");
                        String server = event.getCurrentItem().getItemMeta().getDisplayName().replaceAll(colorFull, "").replaceAll(colorEmpty, "");
                        Utils.sendPlayer(player.getUniqueId(), server);
                        player.closeInventory();
                    }
                }
            });
        }
    }

}
