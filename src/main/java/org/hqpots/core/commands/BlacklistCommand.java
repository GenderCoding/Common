package org.hqpots.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.hqpots.core.utils.StringUtil;

public class BlacklistCommand implements CommandExecutor
{
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
	{
		if (sender.hasPermission("blacklist.command"))
		{
			if (args.length != 1)
			{
				sender.sendMessage(StringUtil.colorize("&cThis is the proper usage: /blacklist <player>"));
			}
			else if (args.length == 1)
			{
				sender.sendMessage(StringUtil.colorize("&cYou are going to blacklist &4" + args[0] + "&c."));
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ipban -s " + args[0] + " You have been blacklisted from our network");
				Bukkit.broadcastMessage(StringUtil.colorize("&4" + args[0] + "&c has been blacklisted from our network."));
				if ((Bukkit.getPlayer(args[0]) != null) && (Bukkit.getPlayer(args[0]).isOnline()))
				{
					Bukkit.getPlayer(args[0]).kickPlayer(StringUtil.colorize("&cYou have been blacklisted from our network!"));
				}
			}
		}
		else
		{
			sender.sendMessage(StringUtil.colorize("&cYou don't have permissions to use this command."));
		}
		return false;
	}
}