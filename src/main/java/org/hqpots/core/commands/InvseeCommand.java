package org.hqpots.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hqpots.core.utils.StringUtil;

public class InvseeCommand implements CommandExecutor
{
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
	{
		Player p = (Player) sender;
		if (!sender.hasPermission("command.invsee"))
		{
			sender.sendMessage(StringUtil.colorize("&cYou do not have permission to use this command."));
			return true;
		}
		if (args.length == 0)
		{
			p.sendMessage(StringUtil.colorize("&cUsage: /invsee [player]"));
		}
		else if (args.length == 1)
		{
			Player target = Bukkit.getServer().getPlayer(args[0]);
			if (!target.isOnline())
			{
				p.sendMessage(StringUtil.colorize("&c" + args[0] + " is not online."));
				return true;
			}
			p.openInventory(target.getInventory());
			p.sendMessage(StringUtil.colorize("&cOpening " + target.getName() + "'s inventory."));
		}
		return false;
	}
}
