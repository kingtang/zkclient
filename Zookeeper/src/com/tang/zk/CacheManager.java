package com.tang.zk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.tang.zk.data.Node;
import com.tang.zk.data.ServerNode;
import com.tang.zk.data.ZNode;
import com.tang.zk.factory.ElementFactory;
import com.tang.zk.listener.ZKValueModifyListener;
import com.tang.zk.model.IElement;
import com.tang.zk.store.DataStore;

/**
 * zk Element & zk Server Manager
 * 
 * @author Administrator
 * 
 */
public final class CacheManager {

	public static final String ROOTPATH = "/";

	// zk server cache
	private static Map<String, ServerNode> serverNodes = new HashMap<String, ServerNode>();

	// zk节点缓存，
	private static Map<String/**serverName.*/, Map<String/** zkPath */
	, IElement<Node>>> elements = new ConcurrentHashMap<String, Map<String, IElement<Node>>>();

	private static IElement<Node> virtualRoot;
	
	private static ZKValueModifyListener modifyListener;
	
	public static ServerNode createServerNode(String ip, String port,
			String name,String userName,String password) {
		ServerNode sNode = new ServerNode();
		sNode.setIp(ip);
		sNode.setPort(port);
		sNode.setName(name);
		sNode.setUserName(userName);
		sNode.setPassword(password);
		// add to cache
		serverNodes.put(name, sNode);
		//存储到文件
		DataStore.saveServerInfo(sNode);
		return sNode;
	}

	public static IElement<Node> deleteServerNode(String serverName)
	{
		ServerNode server = serverNodes.remove(serverName);
		DataStore.deleteServerInfo(server);
		
		return elements.remove(serverName).get(serverName);
	}
	
	public static void addServerNode(ServerNode server) {
		if (null != server) {
			serverNodes.put(server.getName(), server);
		}
	}

	public static ServerNode getServerNode(String name) {
		return serverNodes.get(name);
	}

	public static List<ServerNode> getServerNodes() {
		List<ServerNode> serverCache = new ArrayList<ServerNode>();
		Iterator<Entry<String, ServerNode>> iterator = serverNodes.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			serverCache.add(iterator.next().getValue());
		}
		return serverCache;
	}

	public static IElement<Node> getElement(String serverName, String key) {
		Map<String, IElement<Node>> map = elements.get(serverName);
		if (null != map) {
			return map.get(key);
		}

		return null;
	}

	public static IElement<Node> addElement(String serverName, String fullPath) {
		Map<String, IElement<Node>> singleServerElements = elements
				.get(serverName);
		if (null == singleServerElements) {
			singleServerElements = new ConcurrentHashMap<String, IElement<Node>>();
			elements.put(serverName, singleServerElements);
		}
		IElement<Node> element = ElementFactory.createZKElement(serverName, fullPath);
		singleServerElements.put(fullPath, element);
		
		return element;
	}

	public static void updateElement(String serverName,String key, Object data) {
		IElement<Node> element = elements.get(serverName).get(key);
		Node node = element.getNode();
		if (node instanceof ZNode) {
			if(((ZNode) node).getPath().equals(modifyListener.getPath()))
			{
				modifyListener.setOriginalValue((String)data);
			}
			// 更新缓存
			((ZNode) node).setData(data);
		}
	}
	
	public static IElement<Node> deleteElement(String serverName,String key)
	{
		Map<String, IElement<Node>> singleSeverEle = elements.get(serverName);
		return singleSeverEle.remove(key);
	}

	public static ZKValueModifyListener getModifyListener() {
		return modifyListener;
	}

	public static void setModifyListener(ZKValueModifyListener modifyListener) {
		CacheManager.modifyListener = modifyListener;
	}

	public static IElement<Node> getVirtualRoot() {
		return virtualRoot;
	}

	public static void setVirtualRoot(IElement<Node> virtualRoot) {
		CacheManager.virtualRoot = virtualRoot;
	}

}
