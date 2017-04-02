package org.hqpots.core.redis;

import org.bukkit.Bukkit;
import org.hqpots.core.Core;
import org.hqpots.core.utils.StringUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Protocol;

public class HQJedis extends JedisPubSub
{

	public Jedis jedis = null;
	private RedisConfig rConfig = Core.getRedisConfig();

	@SuppressWarnings("resource")
	public HQJedis()
	{
		final Jedis pubSubJedis = new Jedis(rConfig.getHost(), Protocol.DEFAULT_PORT, Protocol.DEFAULT_TIMEOUT);
		if (rConfig.hasSecurity())
		{
			pubSubJedis.auth(rConfig.getPassword());
		}
		Thread thread = new Thread(() -> {
			pubSubJedis.subscribe(this, "hq_staffchat", "hq_commandspy", "on", "off");
		});
		thread.start();
		if (getJedis().isConnected())
		{
			Bukkit.getConsoleSender().sendMessage(StringUtil.colorize("&aMOTHER FUCKING REDIS CONNECTED BITCHES"));
			send("on", Bukkit.getServerName());
		}
	}

	public void send(String channel, String message)
	{
		try
		{
			getJedis().publish(channel, message);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public Jedis getJedis()
	{
		if (jedis == null)
		{
			jedis = Core.getPool().getResource();
		}
		if (rConfig.hasSecurity())
		{
			jedis.auth(rConfig.getPassword());
		}
		return jedis;
	}

	@Override
	public void onMessage(String channel, String message)
	{
		if (channel.equals("hq_staffchat"))
		{
			Bukkit.broadcast(message, "command.staffchat");
			Bukkit.getConsoleSender().sendMessage(message);
		}
		else if (channel.equals("hq_commandspy"))
		{
			Bukkit.broadcast(message, "command.commandspy");
		}
		else if (channel.equals("off"))
		{
			Bukkit.broadcast("§8§m------------------------------------", "command.staffchat");
			Bukkit.broadcast("", "command.staffchat");
			Bukkit.broadcast("                §c» §d§lNetwork Disconnection §c«", "command.staffchat");
			Bukkit.broadcast("        §e" + message + " has §cdisconnected §efrom the network", "command.staffchat");
			Bukkit.broadcast("", "command.staffchat");
			Bukkit.broadcast("§8§m------------------------------------", "command.staffchat");
		}
		else if (channel.equals("on"))
		{
			Bukkit.broadcast("§8§m------------------------------------", "command.staffchat");
			Bukkit.broadcast("", "command.staffchat");
			Bukkit.broadcast("                §a» §d§lNetwork Connection §a«", "command.staffchat");
			Bukkit.broadcast("        §e" + message + " has §aconnected §eto the network", "command.staffchat");
			Bukkit.broadcast("", "command.staffchat");
			Bukkit.broadcast("§8§m------------------------------------", "command.staffchat");
		}
	}

}