package com.tang.zk.model;

import java.util.ArrayList;
import java.util.List;

import com.tang.zk.CacheManager;
import com.tang.zk.ZKConnectionManager;
import com.tang.zk.data.Node;
import com.tang.zk.data.RootNode;
import com.tang.zk.data.ServersNode;
/**
 *  根节点
 *   |
 *  \ /
 * MyZKServer
 *   |
 *   .ServerNode
 *   .ZKNode
 *   	|
 *   	.LeafZKNode
 *   	.NotLeafZKNode
 *   		|
 *   		.LeafZKNode
 *    	
 * @author Administrator
 *
 */
public class RootElement<E extends Node> implements IElement<Node>{

	//RootElement和serverName绑定
	private String serverName;
	
	public RootElement(String serverName)
	{
		this.serverName = serverName;
	}
	
	//root根节点的子元素有两个，一个是servers node,一个是zk root node
	public List<IElement<Node>> getChildren() {
		System.out.println("get root node children.");
		
		List<IElement<Node>> children = new ArrayList<IElement<Node>>();
		if(null == CacheManager.getServerNode(serverName))
		{
			return children;
		}
		ZKServersElement<ServersNode> serversNode = new ZKServersElement<ServersNode>(this,serverName);
		//添加servers node
		children.add(serversNode);
		
		if(ZKConnectionManager.getInstance().isConnected(serverName))
		{
			//zk根节点
			IElement<Node> zkElement = CacheManager.addElement(serverName, CacheManager.ROOTPATH);
			children.add(zkElement);
		}
		
		return children;
	}

	//无父节点
	public IElement<Node> getParent() {
		return CacheManager.getVirtualRoot();
	}

	public Node getNode() {
		RootNode root = new RootNode();
		root.setName(serverName);
		return root;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

}
