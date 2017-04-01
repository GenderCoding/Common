package org.hqpots.core.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hqpots.core.utils.ColorUtils;

public class ClearChatCommand
  implements CommandExecutor
{
  public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments)
  {
    if (sender.hasPermission("command.clearchat"))
    {
      if (arguments.length == 0) {
        sender.sendMessage(new ColorUtils().translateFromString("&cUsage: /" + label + " <reason>"));
      } else {
        for (Player online : Bukkit.getServer().getOnlinePlayers())
        {
          online.sendMessage(new String[101]);
          online.sendMessage(new ColorUtils().translateFromString("&a" + sender.getName() + " &ehas cleared the global chat for &7" + StringUtils.join(arguments, ' ') + "&e."));
        }
      }
    }
    else {
      sender.sendMessage(new ColorUtils().translateFromString("&cYou do not have permissions to execute this command."));
    }
    return true;
  }
}
