package com.tang.zk.model;

import java.util.List;

import com.tang.zk.data.Node;

public interface IElement<E extends Node> {

	List<IElement<E>> getChildren();

	IElement<E> getParent();

	E getNode();
}
