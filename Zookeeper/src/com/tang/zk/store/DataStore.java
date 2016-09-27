package com.tang.zk.store;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import com.tang.zk.data.ServerNode;

public final class DataStore {
	
	public static final String USER_NAME = System.getProperty("user.name");
	
	public static final String PATH = "C:\\Users\\" + USER_NAME +"\\AppData\\Local\\Temp";
	
	private static List<ServerNode> serverNodes = new ArrayList<ServerNode>();
	
	private static volatile Boolean isInited = false;
	
	public static void saveServerInfo(ServerNode server)
	{
		if(null == server)
		{
			return;
		}
		File cacheDir = new File(PATH);
		if(!cacheDir.exists())
		{
			System.out.println("cache dir not exists.");
			cacheDir.mkdirs();
		}
		File cache = new File(PATH + "\\zkserver.properties");
		if(!cache.exists())
		{
			try {
				boolean success = cache.createNewFile();
				if(!success)
				{
					return;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		operateCacheFile(server,new Action() {
			
			public void doOperateion(Properties properties,ServerNode server) {
				properties.put(server.getName(), server.toString());
			}
		},cache);
	}
	
	private static void operateCacheFile(ServerNode server,Action action,File cache)
	{
		if(!cache.exists())
		{
			return;
		}
		Properties properties = new Properties();
		BufferedReader br = null;
		FileOutputStream fos = null;
		try {
			br = new BufferedReader(new FileReader(cache));
			//将缓存的数据加载到Properties中
			properties.load(br);
			fos = new FileOutputStream(cache);
			action.doOperateion(properties,server);
			properties.store(fos, "server info");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			closeResource(br);
			closeResource(fos);
		}
	}
	
	/**
	 * Action接口用于操作属性文件
	 * @author Administrator
	 *
	 */
	private interface Action
	{
		void doOperateion(Properties properties,ServerNode server);
	}
	
	public static void deleteServerInfo(ServerNode server)
	{
		if (null == server)
		{
			return;
		}
		File cache = new File(PATH + "\\zkserver.properties");
		operateCacheFile(server,new Action() {
			public void doOperateion(Properties properties,ServerNode server) {
				properties.remove(server.getName());
			}
		},cache);
	}

	public static List<ServerNode> getCacheData()
	{
		File cache = new File(PATH + "\\zkserver.properties");
		if(!cache.exists())
		{
			return serverNodes;
		}
		Properties properties = new Properties();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(cache));
			//将缓存的数据加载到Properties中
			properties.load(br);
			
			Iterator<Entry<Object, Object>> iterator = properties.entrySet().iterator();
			while(iterator.hasNext())
			{
				Entry<Object, Object> entry = iterator.next();
				serverNodes.add(getServerNode((String)entry.getValue()));
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			closeResource(br);
		}
		return serverNodes;
	}
	
	private static ServerNode getServerNode(String value) {
		ServerNode node = new ServerNode();
		if(null != value)
		{
			String[] split = value.split(",");
			if(null != split)
			{
				for (String prop : split) {
					String[] keyValue = prop.split("=");
					if(keyValue.length == 2)
					{
						String propName = keyValue[0];
						String propValue = keyValue[1];
						setFiledValue(node, propName.trim(), propValue.trim());
					}
				}
			}
		}
		return node;
	}

	private static void setFiledValue(ServerNode node, String propName,
			String propValue) {
		try {
			Field field = node.getClass().getDeclaredField(propName);
			field.setAccessible(true);
			field.set(node, propValue);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private static void closeResource(Closeable br) {
		if(null != br)
		{
			try {
				br.close();
			} catch (IOException e) {
				br = null;
				e.printStackTrace();
			}
		}
	}

	public static List<ServerNode> getServerNodes() {
		synchronized (isInited) {
			if(isInited)
			{
				return serverNodes;
			}
			else
			{
				isInited = true;
				return getCacheData();
			}
		}
	}

	public static void setServerNodes(List<ServerNode> serverNodes) {
		DataStore.serverNodes = serverNodes;
	}
	
}
