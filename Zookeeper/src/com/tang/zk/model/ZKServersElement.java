package com.tang.zk.model;

import java.util.ArrayList;
import java.util.List;

import com.tang.zk.data.Node;
import com.tang.zk.data.ServerNode;
import com.tang.zk.data.ServersNode;
/**
 * zkServer节点，没有子节点
 * @author Administrator
 *
 */
public class ZKServersElement<E extends Node> implements IElement<Node> {

	private IElement<Node> parent;

	private String serverName;
	
	public ZKServersElement(IElement<Node> parent,String serverName) {
		this.parent = parent;
		this.serverName = serverName;
	}

	public List<IElement<Node>> getChildren() {
		List<IElement<Node>> servers = new ArrayList<IElement<Node>>();
		ZKServerElement<ServerNode> serversNode = new ZKServerElement<ServerNode>(this,serverName);
		servers.add(serversNode);
		return servers;
	}

	public IElement<Node> getParent() {
		return parent;
	}

	public ServersNode getNode() {
		return new ServersNode();
	}

}
