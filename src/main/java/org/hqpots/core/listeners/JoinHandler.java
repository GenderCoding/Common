package org.hqpots.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.hqpots.core.Core;
import org.hqpots.core.utils.StringUtil;

public class JoinHandler implements Listener
{
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void join(PlayerJoinEvent event)
	{
		if (!event.getPlayer().hasPermission("ipmatcher.authentication")) { return; }
		if (Core.getInstance().getDB().getIp(event.getPlayer()) == null)
		{
			Core.getInstance().getDB().setIp(event.getPlayer(), event.getPlayer().getAddress().getAddress().getHostAddress());
			event.getPlayer().sendMessage(StringUtil.colorize("&aLooks like it's your first time joining as staff, you have been added to the authentication system."));
			return;
		}
		if (Core.getInstance().getDB().getResetQueue().contains(event.getPlayer().getUniqueId().toString()))
		{
			event.getPlayer().sendMessage(StringUtil.colorize("&aLooks like an admin reset your IP. Welcome, &e" + event.getPlayer().getName() + "&a."));
			Core.getInstance().getDB().setIp(event.getPlayer(), event.getPlayer().getAddress().getAddress().getHostAddress());
			Core.getInstance().getDB().delFromResetQueue(event.getPlayer());
			return;
		}
		if (!Core.getInstance().getDB().getIp(event.getPlayer()).equals(event.getPlayer().getAddress().getAddress().getHostAddress()))
		{
			event.getPlayer().kickPlayer(StringUtil.colorize("&cAuthentication failed! Looks like your IP has changed, please contact an admin."));
			return;
		}
		event.getPlayer().sendMessage(StringUtil.colorize("&aAuthentication successful, welcome &e" + event.getPlayer().getName() + "&a."));
	}
}
