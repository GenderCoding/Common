package org.hqpots.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class MuteChatCommand implements CommandExecutor {
	
	public static boolean muteToggle = false;
	
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		Player player = (Player) commandSender;
		if((command.getName().equalsIgnoreCase("mutechat")) || (args.length !=0)) {
			if(!player.hasPermission("command.mutechat")) {
				player.sendMessage(ChatColor.RED + "No");
				return true;
			}
			if((!muteToggle) && (player.hasPermission("command.mutechat.toggle"))) {
				muteToggle = true;
				Bukkit.broadcastMessage(ChatColor.GREEN + "Chat has been muted by: " + ChatColor.WHITE + player.getName() + ChatColor.GREEN + ".");
			} 
			else if((muteToggle) && (player.hasPermission("command.mutechat.toggle"))) {
				muteToggle = false;
				Bukkit.broadcastMessage(ChatColor.GREEN + "Chat has been unmuted by: " + ChatColor.WHITE + player.getName() + ChatColor.GREEN + ".");
			}
		}
		return false;
	}
}
