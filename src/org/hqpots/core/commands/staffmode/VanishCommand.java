package org.hqpots.core.commands.staffmode;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.hqpots.core.Core;
import org.hqpots.core.utils.ColorUtils;

public class VanishCommand implements CommandExecutor, TabCompleter
{
  private final Core utilities = Core.getInstance();
    
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments)
  {
    if (!(sender instanceof Player))
    {
      sender.sendMessage(new ColorUtils().translateFromString("&cYou can not execute this command on console."));
      return false;
    }
    
    Player player = (Player) sender;
    if (player.hasPermission("command.vanish"))
    {
      if (arguments.length > 0)
      { 
        player.sendMessage(new ColorUtils().translateFromString("&cUsage: /" + label));
        return true;
      }
      
      if (arguments.length == 0)
      {
        if (utilities.getStaffModeListener().isVanished(player))
        {
          utilities.getStaffModeListener().setVanished(player, false);
          player.sendMessage(new ColorUtils().translateFromString("&eYou have &cdisabled &eyour vanish mode."));
        }
        else
        {
          utilities.getStaffModeListener().setVanished(player, true);
          player.sendMessage(new ColorUtils().translateFromString("&eYou have &aenabled &eyour vanish mode."));
        }
      }
    }
    else
    {
      player.sendMessage(new ColorUtils().translateFromString("&cYou do not have permissions to execute this command."));
    }
    
    return true;
  }
  
  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arguments)
  {
    if (arguments.length > 1)
    {
      return Collections.emptyList();
    }
    return null;
  }
}