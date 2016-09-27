package com.tang.zk.model;

import java.util.ArrayList;
import java.util.List;

import com.tang.zk.CacheManager;
import com.tang.zk.data.Node;
import com.tang.zk.data.ZNode;

/**
 * zk节点元素
 * 
 * @author Administrator
 *
 * @param <E>
 */
public class ZKElement<E extends Node> implements IElement<Node> {

	private ZNode zNode;
	private String serverName;
	public ZKElement(String serverName)
	{
		this.serverName = serverName;
	}
	
	public List<IElement<Node>> getChildren() {
		//返回path的子节点
		List<String> children = zNode.getChildren();
		List<IElement<Node>> childElements = new ArrayList<IElement<Node>>();
		if(null != children)
		{
			for (String path : children) {
				String fullPath = null;
				//获取zk全路径
				if(zNode.getPath().equals("/"))
				{
					fullPath = zNode.getPath() + path;
				}
				else
				{
					fullPath = zNode.getPath() + "/" + path;
				}
				//根据全路径获取zk节点缓存
				IElement<Node> child = CacheManager.getElement(serverName,fullPath);
				if(null == child)
				{
					child = CacheManager.addElement(serverName, fullPath);
				}
				childElements.add(child);
			}
			
		}
		return childElements;
	}

	public IElement<Node> getParent() {
		String path = zNode.getPath();
		//如果是zk根节点，则返回RootElement
		if(path.equals(CacheManager.ROOTPATH))
		{
			return CacheManager.getElement(serverName, "main");
		}
		else//返回节点的父节点
		{
			return CacheManager.getElement(serverName, getParentPath(path));
		}
	}

	private String getParentPath(String path) {
		int index = path.lastIndexOf("/");
		//考虑/config的情况
		if(index == 0)
		{
			index = 1;
		}
		String parentPath = path.substring(0,index);
		return parentPath;
	}

	public Node getNode() {
		return zNode;
	}
	
	public boolean isLeaf()
	{
		return (null == zNode.getChildren() || zNode.getChildren().size() == 0 );
	}

	public void setZNode(ZNode zNode) {
		this.zNode = zNode;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
}
