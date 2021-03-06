package org.hqpots.core.commands;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.hqpots.core.Core;
import org.hqpots.core.utils.StringUtil;

public class StaffModeCommand implements CommandExecutor, TabCompleter
{
	private final Core utilities = Core.getInstance();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments)
	{
		if (!(sender instanceof Player))
		{
			sender.sendMessage(StringUtil.colorize("&cYou can not execute this command in console."));
			return false;
		}

		Player player = (Player) sender;
		if (player.hasPermission("command.staffmode"))
		{
			if (utilities.getStaffModeListener().isStaffModeActive(player))
			{
				utilities.getStaffModeListener().setStaffMode(player, false);
				player.sendMessage(StringUtil.colorize("&eYou have &cdisabled &eyour staffmode."));
			}
			else
			{
				utilities.getStaffModeListener().setStaffMode(player, true);
				player.sendMessage(StringUtil.colorize("&eYou have &aenabled &eyour staffmode."));
			}
		}
		else
		{
			player.sendMessage(StringUtil.colorize("&cYou do not have permissions to execute this command."));
		}

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arguments)
	{
		if (arguments.length > 1) { return Collections.emptyList(); }
		return null;
	}
}