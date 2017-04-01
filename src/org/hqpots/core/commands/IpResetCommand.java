package org.hqpots.core.commands;


import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hqpots.core.Core;

public class IpResetCommand
  implements CommandExecutor
{
  public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3)
  {
    if (!arg0.hasPermission("command.ipmatcher.admin"))
    {
      arg0.sendMessage("§fUnknown command, type \"help\" for help.");
      return true;
    }
    if (arg1.getName().equalsIgnoreCase("ipreset"))
    {
      if (arg3.length != 1)
      {
        arg0.sendMessage("§cCorrect usage: " + arg1.getUsage());
        return true;
      }
      if (arg3[0] == null)
      {
        arg0.sendMessage("§cCorrect usage: " + arg1.getUsage());
        return true;
      }
      Player p = Bukkit.getPlayer(arg3[0]);
      OfflinePlayer ofp = Bukkit.getOfflinePlayer(arg3[0]);
      if ((p == null) && (ofp == null))
      {
        arg0.sendMessage("§cCorrect usage: " + arg1.getUsage());
        return true;
      }
      if (p != null)
      {
        Core.getInstance().getDB().addToResetQueue(p);
        arg0.sendMessage("§aYou have added §f" + p.getName() + " §ato the reset queue.");
      }
      else
      {
        Core.getInstance().getDB().addToResetQueue(ofp);
        arg0.sendMessage("§aYou have added §f" + ofp.getName() + " §ato the reset queue.");
      }
      return true;
    }
    return true;
  }
}
