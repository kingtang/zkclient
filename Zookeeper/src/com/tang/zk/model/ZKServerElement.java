package com.tang.zk.model;

import java.util.ArrayList;
import java.util.List;

import com.tang.zk.CacheManager;
import com.tang.zk.data.Node;
import com.tang.zk.data.ServerNode;
/**
 * zkServer节点，没有子节点
 * @author Administrator
 *
 */
public class ZKServerElement<E extends Node> implements IElement<Node> {

	private IElement<Node> parent;

	private String serverName;
	public ZKServerElement(IElement<Node> parent,String serverName) {
		this.parent = parent;
		this.serverName = serverName;
	}

	public List<IElement<Node>> getChildren() {
		return new ArrayList<IElement<Node>>();
	}

	public IElement<Node> getParent() {
		return parent;
	}

	public ServerNode getNode() {
		return CacheManager.getServerNode(serverName);
	}

}
