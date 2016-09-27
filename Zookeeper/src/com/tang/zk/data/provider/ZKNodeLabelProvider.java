package com.tang.zk.data.provider;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import com.tang.zk.data.Node;
import com.tang.zk.model.IElement;

public class ZKNodeLabelProvider implements ILabelProvider{

	public void addListener(ILabelProviderListener listener) {
		
	}

	public void dispose() {
		
	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
		
	}

	public Image getImage(Object element) {
		@SuppressWarnings("unchecked")
		IElement<Node> e = (IElement<Node>)element;
		return e.getNode().getImage();
	}

	public String getText(Object element) {
		@SuppressWarnings("unchecked")
		IElement<Node> e = (IElement<Node>)element;
		return e.getNode().getText();
	}
	
	

}
