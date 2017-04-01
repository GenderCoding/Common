package org.hqpots.core.redis;

public class RedisConfig
{
	
	public boolean auth;
	public String host;
	public String password;
	
	public RedisConfig(String host, String password)
	{
		this.host = host;
		if(!(password != null)){
			this.password = password;
		}
		this.auth = false;
	}
	
	public String getHost(){
		return host;
	}
	
	public String getPassword(){
		return password;
	}
	
	public boolean hasSecurity(){
		return auth;
	}

}
