package org.hqpots.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.hqpots.core.Core;
import org.hqpots.core.utils.StringUtil;

public class ListCommand implements CommandExecutor
{
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		int users = Bukkit.getOnlinePlayers().size();
		StringBuilder sb = new StringBuilder();
		Core.getInstance();
		if (Core.staff.isEmpty())
		{
			sender.sendMessage(StringUtil.colorize("&7&m-------------------------------------------------"));
			sender.sendMessage(StringUtil.colorize("&eThere are &6&l" + users + " &eplayers online out of a maximum of &6&l" + Bukkit.getServer().getMaxPlayers() + "&e."));
			sender.sendMessage(StringUtil.colorize(""));
			sender.sendMessage(StringUtil.colorize("&dThere are currently no staff online."));
			sender.sendMessage(StringUtil.colorize("&7&m-------------------------------------------------"));
			return true;
		}
		Core.getInstance();
		for (String name : Core.staff)
		{
			sb.append("&d" + name + "&7, ");
		}
		sender.sendMessage(StringUtil.colorize("&7&m-------------------------------------------------"));
		sender.sendMessage(StringUtil.colorize("&eThere are &6&l" + users + " &eplayers online out of a maximum of &6&l" + Bukkit.getServer().getMaxPlayers() + "&e."));
		sender.sendMessage(StringUtil.colorize(""));
		sender.sendMessage(StringUtil.colorize("&eStaff Online: " + sb.toString()));
		sender.sendMessage(StringUtil.colorize("&7&m-------------------------------------------------"));
		return true;
	}
}
