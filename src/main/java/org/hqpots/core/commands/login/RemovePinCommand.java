package org.hqpots.core.commands.login;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hqpots.core.utils.StringUtil;

public class RemovePinCommand implements CommandExecutor
{
	public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments)
	{
		Player player = (Player) sender;
		if (!(sender instanceof Player))
		{
			sender.sendMessage(StringUtil.colorize("&cYou must be a player to execute this command."));
		}
		player.sendMessage(StringUtil.colorize("&cWe are currently developing this feature."));
		return false;
	}
}
