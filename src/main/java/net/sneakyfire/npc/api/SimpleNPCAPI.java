package net.sneakyfire.npc.api;

import com.github.juliarn.npc.NPC;
import com.github.juliarn.npc.SpawnCustomizer;
import com.github.juliarn.npc.modifier.AnimationModifier;
import com.github.juliarn.npc.modifier.MetadataModifier;
import com.github.juliarn.npc.profile.Profile;
import net.sneakyfire.npc.SimpleCloudNPC;
import net.sneakyfire.npc.util.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SimpleNPCAPI {

    private final Location location;
    private final Player player;
    private final String displayName;
    private final String skin;
    private final String configName;
    private final String serverGroup;
    private final boolean lookClose;
    private final boolean imitate;
    private final boolean useRealUUID;
    private final ItemStack itemInHand;
    private Profile profile;
    private NPC npc;

    public SimpleNPCAPI(Player player, Location location, String skin, String displayName, boolean lookClose, boolean imitate, ItemStack itemInHand, String configName, String serverGroup, boolean useRealUUID) {
        this.player = player;
        this.location = location;
        this.skin = skin;
        this.displayName = displayName;
        this.lookClose = lookClose;
        this.imitate = imitate;
        this.itemInHand = itemInHand;
        this.configName = configName;
        this.serverGroup = serverGroup;
        this.useRealUUID = useRealUUID;
    }

    public void createNpc() {
        profile = new Profile(skin);
        if (!profile.complete()) {
            player.sendMessage("§cCould not fetch skin data from given name!");
            return;
        }
        if (displayName.length() > 16) {
            player.sendMessage("§cThe displayname could not be longer than 16 characters!");
            return;
        }
        profile.setName(displayName);
        if (!useRealUUID) {
            profile.setUniqueId(UUID.randomUUID());
        }
        npc = new NPC.Builder(profile).lookAtPlayer(lookClose).imitatePlayer(imitate).location(location).build(SimpleCloudNPC.getInstance().getNpcPool());
        if (itemInHand != null) {
            //npc.equipment().queue(0, itemInHand);
        }
        npc.animation().queue(AnimationModifier.EntityAnimation.SWING_MAIN_ARM);

        if (itemInHand == null) {
            SimpleCloudNPC.getInstance().getNpcFileManager().createNPC(player, configName, location, imitate, lookClose, "null", serverGroup, skin, displayName, useRealUUID);
        } else {
            SimpleCloudNPC.getInstance().getNpcFileManager().createNPC(player, configName, location, imitate, lookClose, itemInHand.getType().name(), serverGroup, skin, displayName, useRealUUID);
        }

        Utils.NPC_CACHE.put(npc, configName);

    }

    public void loadNpc() {
        profile = new Profile(skin);
        if (!profile.complete()) {
            player.sendMessage("§cCould not fetch skin data from given name!");
            return;
        }
        if (displayName.length() > 16) {
            player.sendMessage("§cThe displayname could not be longer than 16 characters!");
            return;
        }
        profile.setName(displayName);
        if (!useRealUUID) {
            profile.setUniqueId(UUID.randomUUID());
        }
        npc = new NPC.Builder(profile).lookAtPlayer(lookClose).imitatePlayer(imitate).location(location).spawnCustomizer((npc, player) -> npc.metadata().queue(MetadataModifier.EntityMetadata.SKIN_LAYERS, true).send(player)).build(SimpleCloudNPC.getInstance().getNpcPool());
        if (itemInHand != null) {
            //npc.equipment().queue(0, itemInHand);
        }
        npc.animation().queue(AnimationModifier.EntityAnimation.SWING_MAIN_ARM);

        Utils.NPC_CACHE.put(npc, configName);

    }

    public Location getLocation() {
        return location;
    }

    public NPC getNpc() {
        return npc;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean shouldImitate() {
        return imitate;
    }

    public boolean shouldLookClose() {
        return lookClose;
    }

    public Profile getProfile() {
        return profile;
    }

    public ItemStack getItemInHand() {
        return itemInHand;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getServerGroup() {
        return serverGroup;
    }

    public String getConfigName() {
        return configName;
    }

    public String getSkin() {
        return skin;
    }
}
