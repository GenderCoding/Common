package org.hqpots.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.hqpots.core.Core;

public class ListListener implements Listener
{
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		Player p = e.getPlayer();
		if (p.hasPermission("command.stafflist"))
		{
			Core.getInstance();
			Core.staff.add(p.getName());
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		Player p = e.getPlayer();

		Core.getInstance();
		if (Core.staff.contains(p.getName()))
		{
			Core.getInstance();
			Core.staff.remove(p.getName());
		}
	}
}