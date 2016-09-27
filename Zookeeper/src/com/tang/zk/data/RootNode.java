package com.tang.zk.data;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
/**
 * 根节点的数据，目前只有image和name
 * @author Administrator
 *
 */
public class RootNode implements Node{

	private String name;
	
	public String getText() {
		return name;
	}

	public Image getImage() {
		return ResourceManager.getImage(ServerNode.class,"/resources/images/png/objects/zookeeper-connection.png");
	}

	public void setName(String name) {
		this.name = name;
	}
}
