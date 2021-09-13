package eu.imposdev.npc.npc.util;

import com.github.juliarn.npc.NPC;
import eu.imposdev.npc.SimpleCloudNPC;
import eu.imposdev.npc.npc.SimpleNPCBuilder;
import eu.imposdev.npc.util.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class NPCCreateUtil {

    private final NPCFileManager npcFileManager = SimpleCloudNPC.getInstance().getNpcFileManager();

    public void createNPC(Player player, String configName, String serverGroup, Location location, boolean shouldImitate, boolean shouldLookClose, UUID skinUUID, Material itemInHand, String displayName, boolean renderSkinLayer) {
        NPC npc = new SimpleNPCBuilder(location).setShouldImitate(shouldImitate).setShouldLookClose(shouldLookClose)
                .setSkinUUID(skinUUID).setItemInHand(itemInHand).setDisplayName(displayName).renderSkinLayers(renderSkinLayer).build();
        npcFileManager.createNPC(player, configName, location, shouldImitate, shouldLookClose, itemInHand.name(), serverGroup, skinUUID, displayName, renderSkinLayer);
        Utils.NPC_CACHE.put(npc, configName);
    }

    public void loadNpcFromConfig() {
        try {
            npcFileManager.getCfg().getConfigurationSection("NPC").getKeys(false).forEach(s -> {
                NPC npc = new SimpleNPCBuilder(npcFileManager.getLocationForNPC(s))
                        .setSkinUUID(UUID.fromString(npcFileManager.getSkinUUID(s)))
                        .setDisplayName(npcFileManager.getDisplayName(s))
                        .setShouldLookClose(npcFileManager.shouldLookClose(s))
                        .setShouldImitate(npcFileManager.shouldImitate(s))
                        .setItemInHand(npcFileManager.getItemInHand(s))
                        .renderSkinLayers(npcFileManager.shouldRenderSkinLayers(s)).build();
                Utils.NPC_CACHE.put(npc, s);
                SimpleCloudNPC.getInstance().getLogger().info("Registered NPC " + s);
            });
        } catch (NullPointerException exc) {
            SimpleCloudNPC.getInstance().getLogger().info("No npcs in config file found!");
            exc.printStackTrace();
        }
    }

}
