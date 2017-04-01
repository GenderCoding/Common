package org.hqpots.core.commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hqpots.core.api.PermissionsAPI;

public class CreativeCommand implements CommandExecutor
{

	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args)
	{
		if (command.getName().equalsIgnoreCase("creative"))
		{
			if (!(commandSender instanceof Player))
			{
				commandSender.sendMessage(PermissionsAPI.CONSOLE);
				return true;
			}

			Player player = (Player) commandSender;

			if (!player.hasPermission("command.creative"))
			{
				player.sendMessage(PermissionsAPI.PERMISSION);
				return true;
			}

			player.setGameMode(GameMode.CREATIVE);
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYour GameMode has been set to&A CREATIVE!"));

		}
		return false;
	}
}
