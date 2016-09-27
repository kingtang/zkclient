package com.tang.zk.data;

import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;

/**
 * zk节点
 * @author Administrator
 *
 */
public class ZNode implements Node{
	
	private String path;
	
	private Object data;
	
	private List<String> children;

	private boolean ephemeral;
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getText() {
		if(path.length() ==1)
		{
			return path;
		}
		return path.substring(path.lastIndexOf("/") + 1);
	}

	public Image getImage() {
		//叶子节点
		if(isLeaf())
		{
			if(isEphemeral())
			{
				return ResourceManager.getImage(ZNode.class, "/resources/images/png/objects/znode-ephemeral.png");
			}
			else
			{
				return ResourceManager.getImage(ZNode.class, "/resources/images/png/objects/znode-leaf.png");
			}
		}
		else
		{
			Image image = ResourceManager.getImage(ZNode.class, "/resources/images/png/objects/znode.png");
			return image;
		}
	}

	public List<String> getChildren() {
		return children;
	}

	public void setChildren(List<String> children) {
		this.children = children;
	}
	
	public boolean isLeaf()
	{
		return (null == getChildren() || getChildren().size() == 0 );
	}

	public boolean isEphemeral() {
		return ephemeral;
	}

	public void setEphemeral(boolean ephemeral) {
		this.ephemeral = ephemeral;
	}
}
