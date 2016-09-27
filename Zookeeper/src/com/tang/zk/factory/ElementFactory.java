package com.tang.zk.factory;

import org.apache.zookeeper.data.Stat;

import com.tang.zk.ZKConnectionManager;
import com.tang.zk.data.Node;
import com.tang.zk.data.ZNode;
import com.tang.zk.model.IElement;
import com.tang.zk.model.RootElement;
import com.tang.zk.model.ZKElement;

public final class ElementFactory {
	
	/**
	 * 创建zk节点
	 * @param serverName
	 * @param fullPath
	 * @return
	 */
	public static IElement<Node> createZKElement(String serverName,String fullPath)
	{
		if(null == serverName || null == fullPath)
		{
			return null;
		}
		IElement<Node> e = null;
		if(serverName.equals(fullPath))
		{
			e = new RootElement<Node>(serverName);
		}
		else
		{
			ZKElement<Node> element = new ZKElement<Node>(serverName);
			ZNode znode = new ZNode();
			znode.setPath(fullPath);
			znode.setChildren(ZKConnectionManager.getInstance().getChildrenPath(fullPath,serverName));
			znode.setData(ZKConnectionManager.getInstance().getZKData(fullPath,serverName));
			znode.setEphemeral(isEphemeral(serverName, fullPath));
			element.setZNode(znode);
			
			e = element;
		}
		
		return e;
	}

	//是否是临时节点，非叶子节点一定不是临时节点
	private static boolean isEphemeral(String serverName, String path) {
		Stat zkMetaData = ZKConnectionManager.getInstance().getZKMetaData(path, serverName);
		if(null == zkMetaData || zkMetaData.getEphemeralOwner() == 0)
		{
			return false;
		}
			
		return true;
	}
}
