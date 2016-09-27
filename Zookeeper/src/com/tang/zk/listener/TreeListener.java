package com.tang.zk.listener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent.Type;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;

import com.tang.zk.CacheManager;
import com.tang.zk.ZKConnectionManager;
import com.tang.zk.data.Node;
import com.tang.zk.data.ZNode;
import com.tang.zk.model.IElement;

/**
 * 树形监听
 * 
 * @author Administrator
 * 
 */
public class TreeListener implements TreeCacheListener {

	private String serverName;
	
	private TreeViewer treeViewer;

	public TreeListener(String serverName) {
		this.serverName = serverName;
	}

	public void childEvent(CuratorFramework client, TreeCacheEvent event)
			throws Exception {
		Type type = event.getType();
		// 更新
		if (type == Type.NODE_UPDATED) {
			System.out.println("update");
			ChildData data = event.getData();
			String path = data.getPath();
			byte[] dataByte = data.getData();
			String value = new String(dataByte, "UTF-8");
			// 更新缓存
			CacheManager.updateElement(serverName, path, value);
		}
		else if (type == Type.NODE_ADDED)
		{
			//新增
			ChildData data = event.getData();
			String path = data.getPath();
			
			CacheManager.addElement(serverName, path);
			if(null != treeViewer)
			{
				String parentPath = getParentPath(path);
				//刷新父节点
				final IElement<Node> parentElement = CacheManager.getElement(serverName, parentPath);
				ZNode node = (ZNode)parentElement.getNode();
				node.setChildren(ZKConnectionManager.getInstance().getChildrenPath(parentPath,serverName));

				//关键代码，此处一定要异步执行，否则UI不会刷新
				Display.getDefault().asyncExec(new Runnable(){
					public void run() {
						treeViewer.refresh(parentElement,true);
					}
				});
			}
		}
		else if (type == Type.NODE_REMOVED)
		{
			//刪除
			ChildData data = event.getData();
			String path = data.getPath();
			CacheManager.deleteElement(serverName, path);
			if(null != treeViewer)
			{
				final String parentPath = getParentPath(path);
				//刷新父节点
				final IElement<Node> parentElement = CacheManager.getElement(serverName, parentPath);
				ZNode node = (ZNode)parentElement.getNode();
				node.setChildren(ZKConnectionManager.getInstance().getChildrenPath(parentPath,serverName));

				//关键代码，此处一定要异步执行，否则UI不会刷新
				Display.getDefault().asyncExec(new Runnable(){
					public void run() {
						treeViewer.refresh(parentElement,true);
					}
				});
			}
		}
	}

	private String getParentPath(String path) {
		if(path.lastIndexOf("/") == 0)
		{
			return "/";
		}
		else
		{
			return path.substring(0,path.lastIndexOf("/"));
		}
	}
	
	public TreeViewer getTreeViewer() {
		return treeViewer;
	}

	public void setTreeViewer(TreeViewer treeViewer) {
		this.treeViewer = treeViewer;
	}

}
