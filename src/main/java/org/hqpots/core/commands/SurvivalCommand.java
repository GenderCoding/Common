package org.hqpots.core.commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hqpots.core.api.PermissionsAPI;

public class SurvivalCommand implements CommandExecutor
{

	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args)
	{
		if (!(commandSender instanceof Player))
		{
			commandSender.sendMessage(PermissionsAPI.CONSOLE);
			return true;
		}

		Player player = (Player) commandSender;
		if (!player.hasPermission("command.survival"))
		{
			player.sendMessage(PermissionsAPI.PERMISSION);
			return true;
		}
		
		player.setGameMode(GameMode.SURVIVAL);
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYour GameMode has been set to&A SURVIVAL!"));

		return false;
	}
}
