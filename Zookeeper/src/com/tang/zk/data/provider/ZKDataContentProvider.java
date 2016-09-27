package com.tang.zk.data.provider;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.tang.zk.model.IElement;

/**
 * ITreeContentProvider
 * @author Administrator
 *
 */
public class ZKDataContentProvider implements ITreeContentProvider {

	public void dispose() {
		System.out.println("ZKDataContentProvidre----dispose().");
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		System.out.println("ZKDataContentProvidre----inputChanged().");
	}

	public Object[] getElements(Object inputElement) {
		return ((IElement<?>)inputElement).getChildren().toArray();
	}

	public Object[] getChildren(Object parentElement) {
		return ((IElement<?>)parentElement).getChildren().toArray();
	}

	public Object getParent(Object element) {
		return ((IElement<?>) element).getParent();
	}

	public boolean hasChildren(Object element) {
		return (((IElement<?>) element).getChildren().size() > 0);
	}

}
