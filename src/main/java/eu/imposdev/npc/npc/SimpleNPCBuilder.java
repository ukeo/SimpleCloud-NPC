package eu.imposdev.npc.npc;

import com.github.juliarn.npc.NPC;
import com.github.juliarn.npc.modifier.EquipmentModifier;
import com.github.juliarn.npc.modifier.MetadataModifier;
import eu.imposdev.npc.SimpleCloudNPC;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class SimpleNPCBuilder {

    private NPC npc;
    private Location location;
    private boolean imitate, lookClose;
    private boolean renderSkinPlayers = true;
    private Material itemInHand;
    private String displayName;
    private UUID skinUUID;

    public SimpleNPCBuilder(Location location) {
        this.location = location;
    }

    public SimpleNPCBuilder setLocation(Location location) {
        this.location = location;
        return this;
    }

    public SimpleNPCBuilder setShouldImitate(boolean imitate) {
        this.imitate = imitate;
        return this;
    }

    public SimpleNPCBuilder setShouldLookClose(boolean lookClose) {
        this.lookClose = lookClose;
        return this;
    }

    public SimpleNPCBuilder renderSkinLayers(boolean renderSkinPlayers) {
        this.renderSkinPlayers = renderSkinPlayers;
        return this;
    }

    public SimpleNPCBuilder setItemInHand(Material material) {
        this.itemInHand = material;
        return this;
    }

    public SimpleNPCBuilder setSkinUUID(UUID skinUUID) {
        this.skinUUID = skinUUID;
        return this;
    }

    public SimpleNPCBuilder setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public NPC build() {
        npc = NPC.builder()
                .profile(SimpleCloudNPC.getInstance().getNpcUtil().createProfile(this.skinUUID, this.displayName))
                .location(this.location)
                .imitatePlayer(this.imitate)
                .lookAtPlayer(this.lookClose)
                .build(SimpleCloudNPC.getInstance().getNpcPool());
        npc.metadata()
                .queue(MetadataModifier.EntityMetadata.SKIN_LAYERS, this.renderSkinPlayers);
        npc.equipment()
                .queue(EquipmentModifier.MAINHAND, new ItemStack(this.itemInHand, 1));

        return npc;
    }

}
