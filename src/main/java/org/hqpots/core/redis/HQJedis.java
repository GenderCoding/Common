package org.hqpots.core.redis;

import org.bukkit.Bukkit;
import org.hqpots.core.Core;

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
			pubSubJedis.subscribe(this, "hq_staffchat");
		});
		thread.start();
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
		if(channel.equals("hq_staffchat")){
			Bukkit.broadcast(message, "command.staffchat");
			System.out.println(message);
		}
	}

}