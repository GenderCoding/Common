package org.hqpots.core.commands.login;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.hqpots.core.Core;
import org.hqpots.core.utils.Color;

public class RemovePinCommand
  implements CommandExecutor
{
  public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments)
  {
    Player player = (Player)sender;
    FileConfiguration data = YamlConfiguration.loadConfiguration(new File(Core.getInstance().getDataFolder(), "pin.yml"));
    if (!(sender instanceof Player)) {
      sender.sendMessage(Color.translate("&cYou must be a player to execute this command."));
    }
    if (label.equalsIgnoreCase("removepin")) {
      player.sendMessage(Color.translate("&cWe are currently developing this feature."));
    }
    return false;
  }
}
