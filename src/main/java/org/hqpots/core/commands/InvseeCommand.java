package org.hqpots.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InvseeCommand implements CommandExecutor
{
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
	{
		Player p = (Player) sender;
		if (!sender.hasPermission("command.invsee"))
		{
			sender.sendMessage("§cYou do not have permission to do this.");
			return true;
		}
		if (args.length == 0)
		{
			p.sendMessage("§c/invsee <player>");
		}
		else if (args.length == 1)
		{
			Player target = Bukkit.getServer().getPlayer(args[0]);
			if (!target.isOnline())
			{
				p.sendMessage("§6'" + target.getName() + "'" + " §fis currently offline.");
				return true;
			}
			p.openInventory(target.getInventory());
			p.sendMessage("§eNow viewing §6" + target.getName() + "'s" + " §einventory.");
		}
		return false;
	}
}
