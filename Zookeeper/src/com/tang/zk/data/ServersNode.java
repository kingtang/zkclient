package com.tang.zk.data;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;

/**
 * servers节点
 * name
 * 		|
 * 		servers
 * 		|
 * 		/
 * @author Administrator
 *
 */
public class ServersNode implements Node{
	
	public String getText() {
		return "Servers";
	}
	public Image getImage() {
		return ResourceManager.getImage(ServersNode.class,"/resources/images/png/objects/servers.png");
	}
}
