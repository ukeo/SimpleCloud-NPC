package net.sneakyfire.npc.commands;

import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.service.ICloudService;
import eu.thesimplecloud.api.servicegroup.ICloudServiceGroup;
import net.sneakyfire.npc.SimpleCloudNPC;
import net.sneakyfire.npc.api.SimpleNPCAPI;
import net.sneakyfire.npc.util.Utils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Create_Command implements CommandExecutor {

    //Player player, Location location, String skin, String displayName, boolean lookClose, boolean imitate, ItemStack itemInHand, String configName, String serverGroup

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("cloud.npc.setup")) {
                if (args.length == 0) {
                    player.sendMessage("§7/cloudnpc reload | Reload all npcs. \n" +
                            "§7/cloudnpc create <skinPlayerName> <displayName> <shouldLookClose> <shouldImitate> <itemInHand> <configName> <serverGroup> <useRealUUID>");
                }
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("reload")) {
                        player.sendMessage("§cTrying to reload npcs...");
                        Utils.reloadNpc(player);
                    }
                }
                if (args.length == 9) {
                    if (args[0].equalsIgnoreCase("create")) {
                        String skinName = args[1];
                        String displayName = args[2].replaceAll("&", "§");
                        boolean lookClose = Boolean.parseBoolean(args[3]);
                        boolean imitate = Boolean.parseBoolean(args[4]);
                        boolean useRealUUID = Boolean.parseBoolean(args[8]);
                        String configName = args[6];
                        String serverGroup = args[7];
                        if (args[5].equalsIgnoreCase("null")) {
                            ICloudServiceGroup serviceGroup = CloudAPI.getInstance().getCloudServiceGroupManager().getServiceGroupByName(serverGroup);
                            if (serviceGroup != null) {
                                SimpleNPCAPI simpleNPCAPI = new SimpleNPCAPI(player, player.getLocation(), skinName, displayName, lookClose, imitate, null, configName, serverGroup, useRealUUID);
                                simpleNPCAPI.createNpc();
                                player.sendMessage("§aNPC created!");
                            } else {
                                player.sendMessage("§cThat servergroup does not exist!");
                            }
                        } else {
                            ICloudServiceGroup serviceGroup = CloudAPI.getInstance().getCloudServiceGroupManager().getServiceGroupByName(serverGroup);
                            if (serviceGroup != null) {
                                ItemStack itemInHand = new ItemStack(Material.valueOf(args[5]));
                                if (itemInHand != null) {
                                    SimpleNPCAPI simpleNPCAPI = new SimpleNPCAPI(player, player.getLocation(), skinName, displayName, lookClose, imitate, itemInHand, configName, serverGroup, useRealUUID);
                                    simpleNPCAPI.createNpc();
                                    player.sendMessage("§aNPC created!");
                                }
                            } else {
                                player.sendMessage("§cThat servergroup does not exist!");
                            }
                        }
                    }
                }
            } else {
                player.sendMessage("§cYou don't have the permission to execute this command!");
            }
        }
        return true;
    }
}
