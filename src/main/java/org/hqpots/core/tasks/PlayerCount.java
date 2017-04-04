package org.hqpots.core.tasks;

import org.bukkit.Bukkit;
import org.hqpots.core.Core;

public class PlayerCount implements Runnable
{

	@Override
	public void run()
	{
		Core.getHqJedis().set(Bukkit.getServerName(), Integer.toString(Bukkit.getOnlinePlayers().size()));
	}
}
