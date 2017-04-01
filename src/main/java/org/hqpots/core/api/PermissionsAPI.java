package org.hqpots.core.api;

import org.bukkit.event.Listener;

import net.md_5.bungee.api.ChatColor;

public class PermissionsAPI implements Listener
{

	public static final String PERMISSION;
	public static final String CONSOLE;
	public static final String DATA;

	static
	{
		PERMISSION = ChatColor.RED + "You dont have perms to use this command";
		CONSOLE = ChatColor.RED + "Dont use this command in console";
		DATA = ChatColor.GREEN + "Your Data has sucsessfully loaded!";
	}
}
