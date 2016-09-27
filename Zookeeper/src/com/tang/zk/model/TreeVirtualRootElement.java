package com.tang.zk.model;

import java.util.ArrayList;
import java.util.List;

import com.tang.zk.CacheManager;
import com.tang.zk.data.Node;
import com.tang.zk.data.ServerNode;
/**
 * TreeViewer的虚拟根节点
 * @author Administrator
 *
 */
public class TreeVirtualRootElement implements IElement<Node>{

	public TreeVirtualRootElement()
	{
	}
	
	//返回多个根节点
	public List<IElement<Node>> getChildren() {
		List<IElement<Node>> children = new ArrayList<IElement<Node>>();
		List<ServerNode> serverNodes = CacheManager.getServerNodes();
		for (ServerNode serverNode : serverNodes) {
			String serverName = serverNode.getName();
			IElement<Node> rootElement = CacheManager.getElement(serverName, serverName);
			if(null == rootElement)
			{
				rootElement = CacheManager.addElement(serverName, serverName);
			}
			children.add(rootElement);
		}
		return children;
	}

	public IElement<Node> getParent() {
		return null;
	}

	public Node getNode() {
		return null;
	}


}
