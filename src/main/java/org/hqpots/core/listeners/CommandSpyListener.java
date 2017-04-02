package org.hqpots.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.hqpots.core.Core;
import org.hqpots.core.utils.StringUtil;

public class CommandSpyListener implements Listener
{

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event)
	{
		String message = StringUtil.colorize("&c[Spy] &e" + event.getPlayer().getName() + "&7: &f" + event.getMessage());
		Core.getHqJedis().send("hq_commandspy", message);
	}

}
