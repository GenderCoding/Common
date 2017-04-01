package org.hqpots.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.hqpots.core.Core;

public class ListCommand implements CommandExecutor
{
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if (cmd.getName().equalsIgnoreCase("list"))
		{
			int users = Bukkit.getOnlinePlayers().size();
			StringBuilder sb = new StringBuilder();
			Core.getInstance();
			if (Core.staff.isEmpty())
			{
				sender.sendMessage("§7§m-------------------------------------------------");
				sender.sendMessage("§eThere are §6§l" + users + " §eplayers online out of a maximum of §6§l" + Bukkit.getServer().getMaxPlayers() + "§e.");
				sender.sendMessage("");
				sender.sendMessage("§dThere are currently no staff online.");
				sender.sendMessage("§7§m-------------------------------------------------");
				return true;
			}
			Core.getInstance();
			for (String name : Core.staff)
			{
				sb.append("§d" + name + "§7, ");
			}
			sender.sendMessage("§7§m-------------------------------------------------");
			sender.sendMessage("§eThere are §6§l" + users + " §eplayers online out of a maximum of §6§l" + Bukkit.getServer().getMaxPlayers() + "§e.");
			sender.sendMessage("");
			sender.sendMessage("§eStaff Online: " + sb.toString());
			sender.sendMessage("§7§m-------------------------------------------------");
		}
		return true;
	}
}
