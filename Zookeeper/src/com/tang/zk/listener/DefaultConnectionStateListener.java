package com.tang.zk.listener;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.listen.ListenerContainer;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;

import com.google.common.base.Function;
import com.tang.zk.ZKConnectionManager;

/**
 * zk状态监听器
 * @author Administrator
 *
 */
public class DefaultConnectionStateListener implements ConnectionStateListener{

	private TreeViewer treeViewer;
	
	private TreeCache treeCache;
	
	private List<TreeCacheListener> treeListeners;
	
	public DefaultConnectionStateListener(TreeViewer treeViewer,TreeCache treeCache)
	{
		this.treeViewer = treeViewer;
		this.treeCache = treeCache;
		treeListeners = new ArrayList<TreeCacheListener>();
		ListenerContainer<TreeCacheListener> listenable = (ListenerContainer<TreeCacheListener>) treeCache.getListenable();
		listenable.forEach(new Function<TreeCacheListener, Void>(){

			public Void apply(TreeCacheListener treeListener) {
				treeListeners.add(treeListener);
				return null;
			}});
	}
	
	public void stateChanged(CuratorFramework client, ConnectionState newState) {
		System.out.println(newState);
		String connectionString = client.getZookeeperClient().getCurrentConnectionString();
		System.out.println("connectionString" + connectionString);
		ZKConnectionManager.getInstance().setState(connectionString, newState);
		if(newState == ConnectionState.LOST)
		{
			System.out.println("zkclient lost with server.");
		}
		else if (newState == ConnectionState.CONNECTED)
		{
			System.out.println("zkclient connect server.");
		}
		else if (newState == ConnectionState.RECONNECTED)
		{
			System.out.println("zkclient lost with server and reconnected.");
			TreeCache treeCache = new TreeCache(client, "/");
			for (TreeCacheListener treeListener : treeListeners) {
				treeCache.getListenable().addListener(treeListener);
			}
			// 启动监听
			try {
				treeCache.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Display.getDefault().asyncExec(new Runnable(){
				public void run() {
					treeViewer.refresh();
				}
			});
			this.treeCache = treeCache;
		}
		else if (newState == ConnectionState.SUSPENDED)
		{
			treeCache.close();
			//关键代码，此处一定要异步执行，否则UI不会刷新
			Display.getDefault().asyncExec(new Runnable(){
				public void run() {
					treeViewer.refresh();
				}
			});
		}
	}

}
