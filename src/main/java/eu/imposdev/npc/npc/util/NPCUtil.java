package eu.imposdev.npc.npc.util;

import com.github.juliarn.npc.NPC;
import com.github.juliarn.npc.modifier.MetadataModifier;
import com.github.juliarn.npc.profile.Profile;
import eu.imposdev.npc.SimpleCloudNPC;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.UUID;

@Getter
public final class NPCUtil {

    private final Random random = new Random();

    /**
     * Creates an example profile for NPCs.
     *
     * @return The new profile
     */
    public Profile createProfile(UUID skinUUID, String name) {
        Profile profile = new Profile(skinUUID);
        // Synchronously completing the profile, as we only created the profile with a UUID.
        // Through this, the name and textures will be filled in.
        profile.complete();

        // we want to keep the textures, but change the name and UUID.
        profile.setName(name);
        // with a UUID like this, the NPC can play LabyMod emotes!
        profile.setUniqueId(new UUID(this.random.nextLong(), 0));

        return profile;
    }

    /**
     * Appends a new NPC to the pool.
     *
     * @param location       The location the NPC will be spawned at
     * @param excludedPlayer A player which will not see the NPC
     */
    public void appendNPC(Location location, UUID skinUUID, String name, boolean imitatePlayer, boolean lookAtPlayer, boolean skinLayers, Player excludedPlayer) {
        // building the NPC
        NPC npc = NPC.builder()
                .profile(this.createProfile(skinUUID, name))
                .location(location)
                .imitatePlayer(imitatePlayer)
                .lookAtPlayer(lookAtPlayer)
                // appending it to the NPC pool
                .build(SimpleCloudNPC.getInstance().getNpcPool());
        // adding skin layer rendering to npc
        npc.metadata()
                .queue(MetadataModifier.EntityMetadata.SKIN_LAYERS, skinLayers);
        if (excludedPlayer != null) {
            // adding the excluded player which will not see the NPC
            npc.addExcludedPlayer(excludedPlayer);
        }
    }

}
