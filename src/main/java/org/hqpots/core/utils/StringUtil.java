package org.hqpots.core.utils;

import net.md_5.bungee.api.ChatColor;

public class StringUtil
{

	public static String join(final String seperator, final String... strings)
	{
		return String.join(seperator, (CharSequence[]) strings);
	}

	public static String colorize(final String string)
	{
		return ChatColor.translateAlternateColorCodes('&', string);
	}

}
