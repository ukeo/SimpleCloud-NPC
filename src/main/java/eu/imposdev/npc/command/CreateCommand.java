package eu.imposdev.npc.command;

import eu.imposdev.npc.SimpleCloudNPC;
import eu.imposdev.npc.util.Utils;
import eu.imposdev.npc.uuid.UUIDFetcher;
import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.servicegroup.ICloudServiceGroup;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CreateCommand extends Command {


    public CreateCommand(String name) {
        super(name);
        setPermission("cloud.npc.create");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if (args.length < 1) {
            player.sendMessage("§7Usage§8: §7/cloudnpc reload | create");
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                Utils.reloadNpc(player);
            }
        }
        if (args.length >= 1 && args.length < 9 && args[0].equalsIgnoreCase("create")) {
            player.sendMessage("§7Usage§8: §7/cloudnpc create <skinName> <displayName> <shouldLookClose> <shouldImitate> <itemInHand> <configName> <serverGroup> <renderSkinLayer>");
        }
        if (args.length == 9) {
            if (args[0].equalsIgnoreCase("create")) {
                UUID skinUUID = UUIDFetcher.getUUID(args[1]);
                String displayName = args[2].replaceAll("&", "§");
                boolean shouldLookClose = Boolean.parseBoolean(args[3]);
                boolean shouldImitate = Boolean.parseBoolean(args[4]);
                boolean renderSkinLayer = Boolean.parseBoolean(args[8]);
                String configName = args[6];
                String serverGroup = args[7];
                if (!args[5].equalsIgnoreCase("null")) {
                    try {
                        Material itemInHand = Material.valueOf(args[5]);
                        ICloudServiceGroup serviceGroup = CloudAPI.getInstance().getCloudServiceGroupManager().getServiceGroupByName(serverGroup);
                        if (serviceGroup != null) {
                            player.sendMessage("§aNPC created!");
                            SimpleCloudNPC.getInstance().getNpcCreateUtil().createNPC(player, configName, serverGroup, player.getLocation(), shouldImitate, shouldLookClose, skinUUID, itemInHand, displayName, renderSkinLayer);
                        } else {
                            player.sendMessage("§cThis servergroup does not exist!");
                        }
                    } catch (Exception exception) {
                        player.sendMessage("§7This material does not exists in your servers version. Server Version: " + SimpleCloudNPC.getInstance().getServerVersion().name());
                        exception.printStackTrace();
                        return true;
                    }
                } else {
                    ICloudServiceGroup serviceGroup = CloudAPI.getInstance().getCloudServiceGroupManager().getServiceGroupByName(serverGroup);
                    if (serviceGroup != null) {
                        player.sendMessage("§aNPC created!");
                        SimpleCloudNPC.getInstance().getNpcCreateUtil().createNPC(player, configName, serverGroup, player.getLocation(), shouldImitate, shouldLookClose, skinUUID, null, displayName, renderSkinLayer);
                    } else {
                        player.sendMessage("§cThis servergroup does not exist!");
                    }
                }
            }
        }
        return true;
    }
}
