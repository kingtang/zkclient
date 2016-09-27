package com.tang.zk.data;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;

/**
 * zk server
 * @author Administrator
 *
 */
public class ServerNode implements Node{
	
	private String ip;
	
	private String port;
	
	private String name;
	
	private String userName;
	
	private String password;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getText() {
		return ip + ":" + port;
	}
	public Image getImage() {
		return ResourceManager.getImage(ServerNode.class,"/resources/images/png/objects/server.png");
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "ip=" + ip + ",port=" + port + ",name=" + name + ",userName=" + userName + ",password=" + password;
	}
}
