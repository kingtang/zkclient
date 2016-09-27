package com.tang.zk;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLBackgroundPathAndBytesable;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.eclipse.jface.viewers.TreeViewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tang.zk.data.ServerNode;
import com.tang.zk.listener.DefaultConnectionStateListener;
import com.tang.zk.listener.TreeListener;

/**
 * 管理ZK连接
 * 
 * @author Administrator
 * 
 */
public final class ZKConnectionManager{

	private static Logger debugLog = LoggerFactory.getLogger("debug");
	
	private static final ZKConnectionManager INSTANCE = new ZKConnectionManager();

	private Map<String, CuratorFramework> zkClients = new HashMap<String, CuratorFramework>();

	private List<TreeListener> treeListeners = new ArrayList<TreeListener>();

	private List<TreeCache> treeCaches = new ArrayList<TreeCache>();
	
	private Map<String/**connectionString.*/,ConnectionState> states = new ConcurrentHashMap<String, ConnectionState>();
	
	public void initClient(ServerNode serverNode,TreeViewer treeViewer) {
		String connectString = serverNode.getIp() + ":"
				+ serverNode.getPort();
		CuratorFramework zkClient = CuratorFrameworkFactory.builder()
				.connectString(connectString).connectionTimeoutMs(1000).sessionTimeoutMs(1000)
				.retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 3000))
				.build();
		// 启动zk连接
		zkClient.start();
		zkClients.put(serverNode.getName(), zkClient);
		
		debugLog.info(serverNode.getName() + " zk client has started.");
		
		TreeListener listener = new TreeListener(serverNode.getName());
		TreeCache treeCache = new TreeCache(zkClient, "/");
		treeCache.getListenable().addListener(listener);
		// 启动监听
		try {
			treeCache.start();
		} catch (Exception e) {
			debugLog.error("start tree cache failed.", e);
		}
		treeListeners.add(listener);
		ConnectionStateListener stateListener = new DefaultConnectionStateListener(treeViewer,treeCache);
		treeCaches.add(treeCache);
		zkClient.getConnectionStateListenable().addListener(stateListener,Executors.newFixedThreadPool(1));
	}

	public static ZKConnectionManager getInstance() {
		return INSTANCE;
	}

	/**
	 * 返回子路径
	 * 
	 * @param parentPath
	 * @return
	 */
	public List<String> getChildrenPath(String parentPath, String serverName) {

		if (null == zkClients.get(serverName).getZookeeperClient()) {
			return null;
		}
		if (zkClients.get(serverName).getZookeeperClient().isConnected()) {
			try {
				return zkClients.get(serverName).getChildren()
						.forPath(parentPath);
			} catch (Exception e) {
				debugLog.error("get children path error.", e);
			}
		}
		return null;

	}

	public boolean isConnected(String serverName)
	{
		String connectionString = zkClients.get(serverName).getZookeeperClient().getCurrentConnectionString();
		ConnectionState currentState = states.get(connectionString);
		if(ConnectionState.CONNECTED == currentState || ConnectionState.RECONNECTED == currentState)
		{
			return true;
		}
		return false;
	}
	
	public void setState(String connectString,ConnectionState state)
	{
		states.put(connectString, state);
	}
	
	public ConnectionState getState(String connectString)
	{
		return states.get(connectString);
	}
	
	public ConnectionState getStateByServerName(String serverName)
	{
		return states.get(this.getZkClient(serverName).getZookeeperClient().getCurrentConnectionString());
	}
	
	//public boolean 
	
	public Object getZKData(String path, String serverName) {
		try {
			byte[] bytes = zkClients.get(serverName).getData().forPath(path);
			if(null != bytes)
			{
				return new String(bytes, Charset.forName("UTF-8"));
			}
		} catch (Exception e) {
			debugLog.error("get zk data error.", e);
		}
		return null;
	}

	public void updateZKData(String path, String serverName, String data) {
		if (null != path && null != data) {
			try {
				zkClients.get(serverName).setData()
						.forPath(path, data.getBytes(Charset.forName("UTF-8")));
			} catch (Exception e) {
				debugLog.error("update zk data error", e);
			}
		}
	}

	public void deleteZKData(String path, String serverName) {
		if (null != path && null != serverName) {
			try {
				zkClients.get(serverName).delete().forPath(path);
			} catch (Exception e) {
				debugLog.error("delete zk data error.", e);
			}
		}
	}
	
	public void deleteZKDataAndChild(String path, String serverName) {
		if (null != path && null != serverName) {
			try {
				zkClients.get(serverName).delete().deletingChildrenIfNeeded().forPath(path);
			} catch (Exception e) {
				debugLog.error("delete zk data and child error.", e);
			}
		}
	}
	
	public void addZKData(String path,String data,String serverName,boolean isEphemeral,boolean isSequential)
	{
		if (null != path && null != serverName) {
			try {
				ACLBackgroundPathAndBytesable<String> builder = getAdjustedBuilder(serverName, isEphemeral, isSequential);
				if(null == data)
				{
					builder.forPath(path);
				}
				else
				{
					builder.forPath(path, data.getBytes(Charset.forName("UTF-8")));
				}
			} catch (Exception e) {
				debugLog.error("add zk node error.", e);
			}
		}
	}

	private ACLBackgroundPathAndBytesable<String> getAdjustedBuilder(
			String serverName,boolean isEphemeral,boolean isSequential) {
		//临时节点
		if(isEphemeral)
		{
			//序列
			if(isSequential)
			{
				return zkClients.get(serverName).create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL);
			}
			else
			{
				return zkClients.get(serverName).create().withMode(CreateMode.EPHEMERAL);
			}
		}
		else
		{
			if(isSequential)
			{
				return zkClients.get(serverName).create().withMode(CreateMode.PERSISTENT_SEQUENTIAL);
			}
			else
			{
				return zkClients.get(serverName).create().withMode(CreateMode.PERSISTENT);
			}
		}
	}

	public Stat getZKMetaData(String path, String serverName) {
		// zkClients.get(serverName).getZookeeperClient().getZooKeeper().
		try {
			Stat stat = zkClients.get(serverName).checkExists().forPath(path);
			return stat;
		} catch (Exception e) {
			debugLog.error("get zk meta data error.", e);
		}
		return null;
	}
	
	public CuratorFramework getZkClient(String serverName) {
		return zkClients.get(serverName);
	}
	
	public void addZkClient(String serverName,CuratorFramework curator) {
		zkClients.put(serverName,curator);
	}

	public void registerTreeViewer(TreeViewer treeViewer) {
		for (TreeListener treeListener : treeListeners) {
			treeListener.setTreeViewer(treeViewer);
		}
	}

	public void close() {
		try {
			for (TreeCache treeCache : treeCaches) {
				treeCache.close();
			}
			Iterator<Entry<String, CuratorFramework>> iterator = zkClients.entrySet().iterator();
			while(iterator.hasNext())
			{
				CuratorFramework client = iterator.next().getValue();
				try {
					client.close();
				} catch (Exception e) {
				}
			}
		} 
		finally
		{
			System.exit(0);
		}
	}
}
